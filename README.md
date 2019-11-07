# SpringFlutter
前情提要
-------
拿破仑曾经说过：“不想当全栈的工程师不是好程序猿”，因此作为普通程序猿的我们，为了能成为一名好的程序猿，要始终坚持学习的步伐。作为我个人来讲，尽管水平一般，但在安卓开发的道路上尝试了原生、phoneGap、RN等各类框架开发，技能深度不敢说，技能点还算丰富，一套技能下来至少撸掉敌人半管子血。这不，为了干掉敌人另外半管子血，踏上了java web开发的学习之路。曾经，曾经啊，我是想成为一名java后台开发的，学着学着ssh，居然阴差阳错踏入了安卓开发道路（这句话不好理解，我也无法解释。。。）。不扯淡了，言归正传，学习之路必然要伴随撸代码的过程，因此就有了这么一个项目，打算做一个前后端分离的电商项目，后台采用Spring boot，也就是本项目。[前端App](https://github.com/shenmengzhuifeng/SpringFlutterApp)使用目前谷歌的亲儿子Flutter开发，也算是在安卓开发的方向上再添一技能。

项目介绍
--------
本项目采用Gradle构建方式，通过token及refreshToken实现鉴权及登录状态维护。后台技能栈包括：Spring boot、MyBatis、Spring Security、redis、Druid、MySql、Kafka等。目前通过Spring Security及JJWT实现了账号的登录认证功能，详见第一章。

项目目录结构
-----------
此处会有截图

目录
---
第一章 账号登录认证<br>
第二章 MyBatis Generator代码生成器


账号登录认证
-----------
任何应用的开发都离不开用户鉴权，用户的权限维护以及api接口的保护是一个系统开发的必备，也是系统的门户。我们就先从账号登录认证开始，一步步构建本系统。目前常用的鉴权方式有基于session和基于token的方式。由于session的验证方式需要session的服务器存储与维护，存在性能缺陷，也不便于集群部署。因此我们直接采用token的认证机制。这里我就在想怎么才能不着痕迹的过渡到jwt及Spring Security的认证方式使用呢，想来想去，实在是没那个文采。。。就直接开始JWT 的token认证方式吧，至于为啥用，网上一搜一大把，JWT使用起来轻便，开销小，服务端不用记录用户状态信息（无状态），解决单点登录问题，使用签名保证信息传输的安全等等。我们这里还是简单的介绍一下JWT<br>

JSON Web Token（JWT）
-------------------
先附上官方解释：JSON Web Token（JWT）是一个开放标准（RFC 7519），它定义了一种紧凑且独立的方式，可以在各方之间作为JSON对象安全地传输信息。此信息可以通过数字签名进行验证和信任。JWT可以使用秘密（使用HMAC算法）或使用RSA或ECDSA的公钥/私钥对进行签名。
虽然JWT可以加密以在各方之间提供保密，但只将专注于签名令牌。签名令牌可以验证其中包含的声明的完整性，而加密令牌则隐藏其他方的声明。当使用公钥/私钥对签署令牌时，签名还证明只有持有私钥的一方是签署私钥的一方。

简单来讲就是JWT可以携带一些用户信息，并且可以进行加密传输。既能反解出用户信息进行识别，又能保证信息的安全可靠。<br>

JWT工作流程
---------
1、用户通过手机app登录注册界面输入用户名、密码或手机号、验证码进行登录（调用后台登录注册接口）<br>
2、服务器验证登录鉴权，如果用户信息合法，根据用户的信息和服务器的规则生成JWT token及JWT refreshToken<br>
3、服务器将token、refreshToken（包括各自的有效期等）以json形式返回。<br>
4、用户得到token及refreshToken等信息，保存于本地及缓存（如SharedPreferences等）<br>
5、以后用户请求protected中的API时，在请求的header中加入 Authorization: Bearer xxxx(token)。此处注意token之前有一个7字符长度的 Bearer，当然也可以不用加<br>
6、当token快过期时，通过refreshToken调用服务端接口刷新token，生成新的有效期token，以续期用户登录状态<br>
7、服务器端受保护接口对token进行检验，如果合法就解析其中内容，根据其拥有的权限和自己的业务逻辑给出对应的响应结果。<br>

流程图如下：



JWT总共由三部分组成，每部分之间通过圆点（.）连接，这三部分分别是Header、Payload、Signature。先来看一个例子：<br>
```java
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3ZWkiLCJjcmVhdGVkIjoxNTcyOTYxNzA3NTk4LCJleHAiOjE1NzQ3NzYxMDd9.Ifpyzix1y4GoNWyQ54zyZa18HHutO-sAF-fhPC-jZjKoLd-Nr0WcGqMTuk6BNR062Fj6lihXNszCdGpk82rktw
```
这个例子总共三部分，分别是:<br>
eyJhbGciOiJIUzUxMiJ9 <br>
eyJzdWIiOiJ3ZWkiLCJjcmVhdGVkIjoxNTcyOTYxNzA3NTk4LCJleHAiOjE1NzQ3NzYxMDd9<br>
Ifpyzix1y4GoNWyQ54zyZa18HHutO-sAF-fhPC-jZjKoLd-Nr0WcGqMTuk6BNR062Fj6lihXNszCdGpk82rktw<br>
三部分每一部分都分别是通过[BASE64](https://en.wikipedia.org/wiki/Base64)编码而成。我们通过Base64解码器（可直接百度在线解析）进行解析分别得到以下三部分:<br>
```java
{"alg":"HS512"}
```
```java
{"sub":"wei","created":1572961707598,"exp":1574776107}
```
```java
!úrÎ,uË¨5lçòe­|{­:À~ÂÊ ·M¯E£ºN5:ØXú(W6ÌÂtjdójä·
```
第一部分告诉我们HMAC采用HS512算法对JWT进行的签名。第二部分可以看出是我们自己需要传递的信息内容。前两部分可以看出全部都是明文，所以不能放置敏感和隐私信息。第三部分是整个jwt的保障，没有秘钥无法解析其内容。三部分内容的具体实现在后面代码部分说明。

JWT的生成和解析
-------------
这里我们引入[JJWT](https://github.com/jwtk/jjwt)这个开源库，用于JWT的生成。JWT的生成可以使用下面这样的代码完成：<br>
```java

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
    
    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据负责生成JWT的refreshToken
     */
    private String generateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateRefreshTokenExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
```
这里方法中的claims参数就是我们要携带在jwt中第二部分的信息，setExpiration设置token过期时间。这里其实两种方式设置都可以，通过把过期时间放到claims里面也可以。我们通过JJWT源码可以很容易理解，Claims类中声明了如下静态变量可供直接使用，
```java
    /** JWT {@code Issuer} claims parameter name: <code>"iss"</code> */
    public static final String ISSUER = "iss";

    /** JWT {@code Subject} claims parameter name: <code>"sub"</code> */
    public static final String SUBJECT = "sub";

    /** JWT {@code Audience} claims parameter name: <code>"aud"</code> */
    public static final String AUDIENCE = "aud";

    /** JWT {@code Expiration} claims parameter name: <code>"exp"</code> */
    public static final String EXPIRATION = "exp";

    /** JWT {@code Not Before} claims parameter name: <code>"nbf"</code> */
    public static final String NOT_BEFORE = "nbf";

    /** JWT {@code Issued At} claims parameter name: <code>"iat"</code> */
    public static final String ISSUED_AT = "iat";

    /** JWT {@code JWT ID} claims parameter name: <code>"jti"</code> */
    public static final String ID = "jti";
```
这些属性既能通过对应方法设置也可以直接通过map设置到claims中。其中signWith方法传入我们自己的秘钥。<br>
解析也很简单，利用 jjwt 提供的parser传入秘钥，然后就可以解析token了。

```java
    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}",token);
        }
        return claims;
    }
```

JWT本身的生成与解析比较简单，重点在于集成到Spring boot中，串联整个权限校验。这里我们通过Spring的一个子项目Spring Security与JJWT结合使用完成本系统的鉴权工作。<br>

Spring Security
---------------
[Spring Security](http://projects.spring.io/spring-security/)是一个基于Spring的通用安全框架,采用了责任链设计模式，它有一条很长的过滤器链。做过Android开发的应该都用过网络请求框架OKHttp，这里的过滤器链就类似OKHttp的各个网络拦截器。这里关于Spring Security的工作原理不在详细介绍（后面有时间或许可以再做下源码解析）。<br>

代码具体实现
----------
功能采用MySql数据库，先创建数据库springflutter，并在数据库springflutter中创建user表。表结构如下：<br>

![](https://github.com/shenmengzhuifeng/SpringFlutter/blob/master/images/user_table.jpg)







































