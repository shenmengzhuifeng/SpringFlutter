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
[Spring Security](http://projects.spring.io/spring-security/)是一个基于Spring的通用安全框架,采用了责任链设计模式，它有一条很长的过滤器链。做过Android开发的应该都用过网络请求框架OKHttp，这里的过滤器链就类似OKHttp的各个网络拦截器。拦截器相关的所有配置均位于WebSecurityConfigurerAdapter类中，可实现如下：<br>
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // Spring会自动寻找同样类型的具体类注入，这里就是JwtUserDetailsServiceImpl了
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }
    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/auth/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }
}
```
配置简介
------
configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder)

AuthenticationManager 的建造器，配置 AuthenticationManagerBuilder 会让Security 自动构建一个 AuthenticationManager；如果想要使用该功能你需要配置一个 UserDetailService 和 PasswordEncoder。UserDetailsService 用于在认证器中根据用户传过来的用户名查找一个用户， PasswordEncoder 用于密码的加密与比对，我们存储用户密码的时候用PasswordEncoder.encode() 加密存储，在认证器里会调用 PasswordEncoder.matches() 方法进行密码比对。如果重写了该方法，Security 会启用 DaoAuthenticationProvider 这个认证器，该认证就是先调用 UserDetailsService.loadUserByUsername 然后使用 PasswordEncoder.matches() 进行密码比对，如果认证成功成功则返回一个 Authentication 对象。

configure(HttpSecurity httpSecurity) 

这个配置方法是整个Spring Security的关键，也是最复杂。本项目中用到的已在上面代码中进行注释，这里唯一要说明的是addFilterBefore方法，指插入对应的过滤器之前，还有addFilterAfter 加在对应的过滤器之后，addFilterAt 加在过滤器同一位置。

代码具体实现
----------
功能采用MySql数据库，先创建数据库springflutter，并在数据库springflutter中创建user表。表结构如下：<br>

![](https://github.com/shenmengzhuifeng/SpringFlutter/blob/master/images/user_table.jpg)

为了方便用户名密码认证，在user表中直接插入了一条数据，后面实现了注册接口之后可先通过注册接口生成用户信息再调用登录接口登录。为了方便，插入数据的密码暂时以明文方式展现，后面将统一密码加密处理。<br>
1、首先创建Spring boot项目SpringFlutter，采用Gradle编译方式，其中最简单的一种是通过一个叫Spring Initializr的在线工具 http://start.spring.io/ 进行工程的生成。也可以通过Intellij IED直接创建。创建成功之后在IDE中打开，并创建module，命名为Code。当然这里你也可以不创建submodule，为了后期代码的目录维护，我这里创建了module目录Code。修改settings.gradle文件如下：<br>
```java
include 'Code'
```
在code目录下的build.gradle下加入如下依赖<br>
```java
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compile('org.springframework.boot:spring-boot-starter-web') //起步依赖
    compile('org.springframework.boot:spring-boot-starter-jdbc')//起步依赖
    compile('com.alibaba:druid-spring-boot-starter:1.1.10')//druid数据源
    compile("org.springframework.boot:spring-boot-starter-security")//Spring Security起步依赖
    compile("org.springframework.security.oauth:spring-security-oauth2")//Spring Security oauth2
    compile("org.springframework.security:spring-security-jwt")
    compile("mysql:mysql-connector-java")
    compile('io.jsonwebtoken:jjwt:0.9.0')//jjwt库
    compile('org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.1')//mybatis起步依赖
    compile('mysql:mysql-connector-java:8.0.11')
    compile('cn.hutool:hutool-all:4.5.7')//方法工具库
}
```
<br>
在application.yml(可在resource文件夹下新建此文件，application.properties文件就不用了，区别可自行百度)文件添加如下内容：
```yml
spring:
  profiles:
    active: dev #默认为开发环境
  datasource:
    #mysql数据库
    url: jdbc:mysql://localhost:3306/springflutter
    #数据库用户名
    username: root
    #密码
    password: 123456
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      initial-size: 5 #连接池初始化大小
      min-idle: 10 #最小空闲连接数
      max-active: 20 #最大连接数
      web-stat-filter:
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*" #不统计这些请求数据
      stat-view-servlet: #访问监控网页的登录用户名和密码
        login-username: druid
        login-password: druid
server:
  port: 8089 #服务运行端口

mybatis:
  mapper-locations:
  - classpath:mapper/*.xml #mybatis 对应的mapper路径

jwt:
  tokenHeader: Authorization #JWT存储的请求头
  secret: mySecret #JWT加解密使用的密钥
  expiration: 1814400 #JWT的超期限时间(60*60*24*3)
  expirationRefreshToken: 54432000 #JWT的超期限时间(60*60*24*90)
  tokenHead: Bearer  #JWT负载中拿到开头

logging:
  level:
    root: info #日志配置DEBUG,INFO,WARN,ERROR
#  file: demo_log.log #配置日志生成路径
#  path: /var/logs #配置日志文件名称

```

一切配置就绪（数据库用户名密码修改为自己的），准备写代码了，首先创建用户表的映射类User<br>
```java
public class User {

    private String loginName;

    private String nickName;

    private String customerId;

    private String headerUrl;

    private String mobilePhone;

    private String password;
```
省略set、get方法，然后创建用户表操作对应的Mapper类与xml文件，UserMapper、UserMapper.xml<br>
```java
@Mapper
public interface UserMapper {
    User selectUserByLoginName(@Param("loginName") String loginName);
}
```
```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tw.cloud.mapper.UserMapper">

    <select id="selectUserByLoginName" parameterType="java.lang.String" resultType="com.tw.cloud.bean.user.User">
        SELECT * FROM user WHERE loginName = #{loginName}
    </select>
</mapper>
```
创建Spring Security中的UserDetailsService实现类<br>
```java
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper mUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mUserMapper.selectUserByLoginName(username);
        if (user != null) {
            return new JwtUserDetail(user);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
```
创建Spring Security中UserDetails的实现类<br>
```java
public class JwtUserDetail implements UserDetails {

    private User mUser;


    public JwtUserDetail(User user){
        this.mUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return mUser.getPassword();
    }

    @Override
    public String getUsername() {
        return mUser.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;//暂未实现
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;//暂未实现
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;//暂未实现
    }

    @Override
    public boolean isEnabled() {
        return false;//暂未实现
    }
```
创建UserService，作为用户相关操作类，与UserDetailService不同，这个实现类会包括所有用户相关操作<br>
```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDetailsService mUserDetailsService;

    @Autowired
    private PasswordEncoder mPasswordEncoder;

    @Autowired
    private JwtTokenUtil mJwtTokenUtil;

    @Value("${jwt.expiration}")
    private Long mExpiration;
    @Value("${jwt.expirationRefreshToken}")
    private Long mExpirationRefreshToken;

    @Override
    public String register() {
        return null;
    }

    @Override
    public CustomerInfoReply login(String username, String password) {
        UserDetails userDetails = mUserDetailsService.loadUserByUsername(username);
//        if(!mPasswordEncoder.matches(password,userDetails.getPassword())){
//            throw new BadCredentialsException("密码不正确");
//        } //这里暂时不对密码进行加密校验
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = mJwtTokenUtil.generateToken(userDetails);
        String refreshToken = mJwtTokenUtil.generateRefreshToken(userDetails);
        return new CustomerInfoReply(token,refreshToken,mExpiration,mExpirationRefreshToken);
    }
```
这里的login方法通过从数据库里面查到的用户名生成jwt token refreshToken，JwtTokenUtil类的具体实现如下。<br>
/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by wei
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.expirationRefreshToken}")
    private Long expirationRefreshToken;

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

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
    /**
     * 生成refreshToken的过期时间
     */
    private Date generateRefreshTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationRefreshToken * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

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
     * 根据用户信息生成token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateRefreshToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}

接下来创建UserController类，添加登录接口映射方法login：<br>
```java
@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService mUserService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO,method = RequestMethod.GET)
    public CommonResp<User> getCustomerInfo(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            User user = userMapper.selectUserByLoginName(username);
            return CommonResp.success(user);
        }else {
            return new CommonResp<User>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1002.getCode()
            ,ResultCode.RESULT_CODE_1002.getMessage(),null);
        }
    }

    @RequestMapping(value = UnifyApiUri.UserApi.API_AUTH_LOGIN,
            method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResp<CustomerInfoReply> login(@RequestBody AuthenticationRequest authenticationRequest) {
        LOGGER.info("login info==>" + authenticationRequest.toString());
        CustomerInfoReply customerInfoReply = mUserService.login(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        LOGGER.info("customerInfoReply info==>" + customerInfoReply.toString());
        return CommonResp.success(customerInfoReply);
    }
}
```

login方法解析<br>
-----------
方法请求方式为post，请求参数设置类型为RequestBody，安卓端采用okhttp的postString方法进行请求。首先从请求参数中获取用户名密码，其中请求body体类如下：

```java
public class AuthenticationRequest extends CommonRequest{
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```
获取到请求的用户名和密码之后调用UserService类的login方法进行登录，登录成功返回CustomerInfoReply<br>
```java
public class CustomerInfoReply {

    private String token;

    private String refreshToken;

    private Long tokenExpireTime;

    private Long refreshTokenExpireTime;
```
整个登录并返回token的具体过程已经结束，拿到返回的token，终端请求相关接口时带上token。<br>
![](https://github.com/shenmengzhuifeng/SpringFlutter/blob/master/images/image_login.jpg)

我们发现UserController类中出来login方法还有一个getCustomerInfo方法用于获取用户详细信息，此方法需要校验token，并通过token里面的loginName查询相关用户信息。Spring Security会在请求到达Controller之前先对token的格式、有效期等做校验。这里我们就需要添加token校验的过滤器，用于校验token，过滤器类实现如下：<br>
```java
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
```
创建JwtAuthenticationTokenFilter之后，为了使其生效，需要将其加入到Spring Security过滤器链中，实现如下：<br>
```java
   // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
```
![](https://github.com/shenmengzhuifeng/SpringFlutter/blob/master/images/image_getCustomerInfo.jpg)

前面对于WebSecurityConfig已经展示，这里不再赘述，这样就实现了token的整个认证流程。

第二章 MyBatis Generator代码生成器
-------------------------------






























