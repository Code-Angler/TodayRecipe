# TodayRecipe(서버)
### 목차
- [개발 배경](#개발-배경)
- [개발 과정](#개발-과정)
  - [개발 기간](#1-개발-기간)
  - [사용 언어](#2-사용-언어)
  - [프로젝트 목표](#3-프로젝트-)
  - [와이어프레임 설계](#4-와이어프레임-설계(Figma-활용))
  - [API 설계](#5-API-설계)
  - [DB 설계](#6-DB-설계)
- [개발 결과](#개발-결과)
  - [구현한 기능](#1구현한-기능)
  - [실제 서비스 모습](#2-실제-서비스-모습)
  - [피드백](#3-회고-및-피드백)
---
### 개발 배경
- 오픈된 공간에서 유저 서로가 맛있는 음식의 레시피를 공유하는 공간입니다!
- 개발 구상 단계에서 저희 팀 4명은 배가 고팠습니다. 맛있는 음식이 먹고싶다는 이야기를 하는 도중 떠오른 아이디어로 개발하게 되었습니다.
- 이번 프로젝트는 각자가 주특기`개인의 선호에 따라 백엔드(Java Spring), 프론트엔드(React) 선택` 를 공부하고 진행하는 첫 협업이었습니다.
---
### 개발 과정

#### 1. 개발 기간
- `2021년 07월 09일(금) ~ 2021년 07월 15일(목) / 총 7일`
- `설계 1일 / 개발 6일`

#### 2. 사용 언어
- **Languages** : Java(Backend), React(Frontend)
- **Framework** : Spring
- **DB** : MySQL, H2(테스트용)

#### 3. 프로젝트 목표
1. 서로 다른 개발환경에서의 연동(CORS)
2. 회원가입 & Spring에서 JWT 방식의 로그인
3. 게시판 구현(CRUD 구현, 이미지 업로드)
4. 댓글 작성(CRUD 개념 적용)
5. 검색
6. 비밀번호 변경

#### 4. 와이어프레임 설계(Figma 활용)
[Figma 설계 보러가기](https://www.figma.com/file/oZF7U6mjOvXaIq3nfeeKKf/%ED%95%AD%ED%95%B499-%EB%AF%B8%EB%8B%88%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-24%EC%A1%B0?node-id=0%3A1)

- 회원가입

![회원가입1](https://user-images.githubusercontent.com/85334989/128336199-996a5d6c-6e70-4e7a-8848-2b622d8f52f4.png)
![회원가입2](https://user-images.githubusercontent.com/85334989/128336294-725691be-8ff4-40e8-a505-3a63f1ddd1fb.png)


2. 로그인

![로그인](https://user-images.githubusercontent.com/85334989/128336344-2b783a07-e22f-48f8-823a-488808dbceff.png)

3. 메인페이지

![메인페이지](https://user-images.githubusercontent.com/85334989/128336417-12e97821-06d1-4a1d-8a6c-2ab194008070.png)

4. 상세 페이지

![상세 페이지](https://user-images.githubusercontent.com/85334989/128336485-dccccaa6-038a-48ac-8755-161eec7a2638.png)

5. 게시글 작성&수정 페이지

![게시글 작성 페이지](https://user-images.githubusercontent.com/85334989/128336550-ab747d6a-5244-4027-aa32-aa8a39805f4e.png)

#### 5. API 설계
- Swagger 2.0을 이용하여 API를 관리하였습니다.
![API 설계](https://user-images.githubusercontent.com/85334989/128338423-100f420c-d2c2-4cfc-ba00-b37bea8eb9d6.png)

#### 6. DB 설계
![image](https://user-images.githubusercontent.com/85334989/128343753-30e5633e-bb42-4cf9-a363-e8756fb2f146.png)


---
### 개발 결과

#### 1.구현한 기능
**1. CORS 문제 해결**

본 프로젝트에서는 프론트엔드(React.js)와 백엔드(Spring)가 각각 다른 환경에서 개발했습니다. 즉, 각 단의 독립적인 서버(도메인)를 열어 작업해야 했습니다.<br></br>
서버단에서는 필요한 API를 모두 생성/관리하고, 프론트엔드에서는 서버에서 구축한 API를 활용하도록 했습니다.<br></br>
이 과정에서 CORS 문제가 발생하기도 했습니다. CORS는 웹개발을 하다가 흔히 만날 수 있는 이슈입니다. 대개는 프론트엔드 개발시에 로컬에서 API 서버에 요청을 보낼 때 흔하게 발생합니다.
서로 다른 도메인간에 자원을 공유하는 것을 뜻합니다. 대부분의 브라우저에서는 이를 기본적으로 차단하며, 서버측에서 헤더를 통해서 사용가능한 자원을 알려준다는 것을 배웠습니다.
```java
//CORS 허용 설정 부분
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://todayrecipe.shop.s3-website.ap-northeast-2.amazonaws.com","http://localhost:3000","http://todayrecipe.shop/")
                //클라이언트 로컬 주소임. 클라이언트에서 내 서버의 api에 접근 시 허용에 관한 부분. CORS.
                //2개 이상의 origin에 대해서 허용할 수 있음!
                .allowedMethods("POST","GET","PUT","DELETE","HEAD","OPTIONS") // 클라이언트에서 요청하는 메소드 어디까지 허용할 것인가.
                .allowCredentials(true);
    }
```

**2. Spring 환경에서 JWT 방식의 로그인**

이번 프로젝트에서 가장 애먹었던 부분입니다. 로그인에 성공한 유저에게 **토큰**을 발행하여 이를 프론트단에 보내고, 프론트단에서는 서버의 API에 접근할 때 이 **토큰**을 포함하고,
서버에서는 받은 **토큰**을 검증하는 단계가 계속 이루어지게 해야 했습니다.

![jwt1](https://user-images.githubusercontent.com/85334989/128339887-413af3a0-65e5-4394-8f07-017923ee2b48.png)
![jwt2](https://user-images.githubusercontent.com/85334989/128339853-11304c4e-44ed-4a82-ac2f-f1464b5eb623.png)

계속 고민했던 부분은 그림의 `6번` 과정이었습니다. 서버에서 토큰 발행하고, 클라이언트에서 토큰을 포함해 요청하는 것 까지는 성공했으나, **서버에서 토큰을 어떻게 받고 검증할 지**가 가장 큰 고민이었습니다. **ARC**라는 API 테스트 툴로 유저 정보 인증이 필요한 API에 request 할 경우, 계속해서 오류가 발생했습니다.<br></br>

개발기간이 총 7일이 주어졌는데, 이 문제를 해결하기 위해서 총 3.5일의 시간이 걸렸습니다.

**오류가 계속 발생했던 원인은 클라이언트에서 서버로 보내는 요청의 Header의 이름이 클라이언트-서버 간에 일치하지 않았기 때문이었습니다**
```java
//서버의 JwtTokenProvider.java
public String resolveToken(HttpServletRequest request) {
     return request.getHeader("X-AUTH-TOKEN");
 }

------------------------------------------------------------------------------

// 서버의 JwtAuthenticationFilter.java
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        System.out.println(token); // 클라이언트에서 받은 token값을 서버 콘솔에 찍어줌.
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // validateToken의 결과가 True라면~~~
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 토큰 인증과정을 거친 결과를 authentication이라는 이름으로 저장해줌.


            SecurityContextHolder.getContext().setAuthentication(authentication);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            // token이 인증된 상태를 유지하도록 context(맥락)을 유지해주는 부분임.
        }
        chain.doFilter(request, response); //request 받아서 filter를 거친 결과를 response로 내려줌.
```
서버에서 지정한 Header의 이름이 `X-AUTH-TOKEN`이었는데, 클라이언트단에서는 Header의 이름을 다르게 지정하여 요청을 보내고 있었습니다.

막혔던 부분을 해결하고, 이후의 인증 과정 또한 정상적으로 이루어졌습니다.

**3. 검색**
검색기능을 추가하기 위해 API를 추가로 생성했습니다.
```java
// Controller
@GetMapping("/api/articles/search")
    public List<ArticleResponseDto> getSearchedComments(@RequestParam("query") String keyword) {
        return articleService.getSearchedArticles(keyword);
    }
// Service
public List<ArticleResponseDto> getSearchedArticles(String keyword) {
        List<Article> articles = articleRepository.findByTitleContaining(keyword);
        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();
        for(Article article : articles){
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(
                    article.getId(),
                    article.getTitle(),
                    article.getUser().getUsername(),
                    article.getContent(),
                    article.getCreatedAt(),
                    article.getModifiedAt(),
                    article.getImageUrl()
            );
            articleResponseDtos.add(articleResponseDto);
        }
        return articleResponseDtos;
    }
```
JPA 문법을 활용하여 `ArticleRepository`에 `findByTitleContaning`이라는 메서드를 추가했습니다. 클라이언트에서 위 API를 요청할 때 포함해서 보내는 검색어가 `Title`에 대응하고, 이 대응된 문자열이 포함된 **Article**을 모두 찾는 메서드입니다.

**4. 비밀번호 변경**
일반적으로 비밀번호를 변경하기 위해서는 **1. 현재 비밀번호 입력**, **2. 변경할 비밀번호 입력**, **3. 변경할 비밀번호 재입력**의 과정을 거치게 됩니다.

이를 구현하기 위해서 **1. 현재 비밀번호 입력**받고 확인하는 코드를 아래와 같이 구현했습니다.
```java
// 유저 비밀번호 확인 Controller
@PostMapping("/myinfo")
    public Map<String, String> checkPassword(@RequestBody Map<String, String> password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new UserRequestException("로그인 한 사용자만 비밀번호 체크를 할 수 있습니다.");
        }
        String dbPassword = userDetails.getPassword();
        String check = password.get("password");
        if (!passwordEncoder.matches(check, dbPassword)) {
            throw new UserRequestException("현재 비밀번호와 일치하지 않습니다.");
        }
        Map<String, String> result = new HashMap<>();
        result.put("result", "현재 비밀번호와 일치합니다");
        return result;
    }
```
위 과정이 통과되어야 새 비밀번호를 입력 가능하도록 만들었습니다. 이후 **2번, 3번** 과정을 검증하기 위해 아래와 같은 코드로 구현했습니다.
```java
////// 유저 비밀번호 변경 요청 Controller//////
    @PutMapping("/myinfo")
    public ResponseEntity<MyInfoResponseDto> editPassword(@RequestBody Map<String, String> newPassword, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new UserRequestException("로그인 한 사용자만 비밀번호 변경을 할 수 있습니다.");
        }
        Map<String, String> result = new HashMap<>();
        String newPass = newPassword.get("newpassword");
        String rePass = newPassword.get("renewpassword");
        if (!newPass.equals(rePass)) {
            throw new UserRequestException("비밀번호가 서로 일치하지 않습니다.");
        }
//        result.put("result", "비밀번호 변경이 완료되었습니다.");
        userService.update(newPass, userDetails.getUser());
//        return result;
        MyInfoResponseDto myInfoResponseDto = MyInfoResponseDto.of(userDetails.getUser());
        return ResponseEntity.ok(myInfoResponseDto);
    }
```

#### 2. 실제 서비스 모습
[시연 영상 YouTube 이동](https://www.youtube.com/watch?v=NF_MD1WTnFI&t=53s)

#### 3. 회고 및 피드백
- 백엔드, 프론트엔드로 분리된 개발환경에서 각자 개발을 하고, 이를 한 개의 결과물로 만드는 과정에서 나타나는 문제(CORS, JWT인증)를 겪고 이를 해결해보는 좋은 경험이었습니다.
- 주특기를 선택하고 공부하기 이전에 프로젝트를 진행할 때에는 프론트엔드, 백엔드 구분 없이 모두가 동일한 Repository의 동일한 file을 갖고 기능단위로 역할을 맡아 개발을 했었습니다. 
- 최초에 그렸던 와이어프레임과 완전히 동일하게(view)는 구현하지 못했지만, 기능적으로는 90% 이상 달성한 뿌듯한 프로젝트입니다.
- Spring 공부를 시작하고 혼자 프로젝트를 진행할 때에는 썩 예쁜 페이지를 만들지 못했지만, 프론트엔드 개발자들과 협업하면서 온전히 백엔드 개발에만 몰입할 수 있었고, 보기에도 예쁜 페이지를 만들 수 있었습니다.
- 이번 프로젝트에서는 `좋아요`, `프로필사진`의 소셜 기능을 담지 못했지만, 추후 더욱 연구해서 꼭 적용해보고 싶다는 생각을 했습니다.
