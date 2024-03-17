package kr.ezen.daangn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.ezen.daangn.service.DaangnMemberService;

@Configuration
@EnableWebSecurity
// 시큐리티 설정이다. 활성화 하겠다. 
// 2.x는  WebSecurityConfigurerAdapter를 상속받지만 3.x는 아무것도 상속받지 않는다.
public class SecurityConfig {

	// SecurityFilterChain객체 등록 : 자원별 접근 권한 설정
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// csrf 토큰을 사용하지 않겠다.
		http.csrf(AbstractHttpConfigurer::disable);
		// 인증 설정
		http.authorizeHttpRequests((authorize) -> {
			authorize
					// mainPage
					.requestMatchers("/", "/dbinit", "/getfleamarketList").permitAll()
					// 지정 주소에 대한 권한 설정 **은 하위폴더 포함 모두
					.requestMatchers("/css/**", "/js/**", "/img/**", "/upload/**").permitAll()
					// 회원가입 폼과 회원가입 완료는 누구나 접근 가능 
					.requestMatchers("/member/login/useridcheck","/member/login","/member/logout","/member/join","/member/joinok","/member/send","/member/findUserName","/member/checkEmailAndUsername","/member/passwordUpdateByUsername","/member/login/usernicknamecheck").permitAll()
					.requestMatchers("/user/**").permitAll()
					// 중고거래 리스트 사이트!
					.requestMatchers("/fleamarket/**","/fleamarketDetail/**").permitAll()
					.requestMatchers("/news/**").permitAll()
					// 생활꿀팁 리스트 사이트!
					.requestMatchers("/smartLivingTips").permitAll()
					// 지정 주소에 대한 권한 설정 : hasRole(권한)은 지정 권한이 있는 사용자만 접근이 가능하다.
					.requestMatchers("/adm", "/adm/**").hasRole("ADMIN")
					// 그 이외의 요청은 인증된 사용자만 접근이 가능하다.
					.anyRequest().authenticated();
		});

		// 사용자 로그인을 사용하겠다 (내가만든 로그인 페이지를 사용하겠다?)
		http.formLogin((form) -> {
			// 로그인 주소
			form.loginPage("/member/login").permitAll()
					// 로그인 폼의 name속성값이 username과 password가 아니라면 변경해준다.
					.usernameParameter("username").passwordParameter("password")
					// 로그인 성공후 이동할 주소 지정
					.defaultSuccessUrl("/")
					// 로그인 성공시 처리할 내용을 추가로 지정 가능하다.
					.successHandler(new LoginSuccessHandler());
		});
		// 로그아웃시 처리 내용 지정
		http.logout((logout) ->
		// 권한 설정
		logout.permitAll()
				// 로그아웃후 이동할 주소 지정
				.logoutSuccessUrl("/")
				// 세션 정보를 지울지 여부
				.invalidateHttpSession(true));
		// 완성해서 리턴(객체 등록)
		return http.build();
	}

	@Autowired
	private DaangnMemberService daangnMemberService;

	// DB에 저장된 비번과 같은지 확인하는 
	// JDBC인증
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	// 4. 시큐리티에서 현재의 VO를 사용하려면 
    	//    여기에서 회원 정보를 가져와 인증 영역에 정보를 저장할 서비스를 등록해 준다.
    	//    등록해주는 서비스는 UserDetailsService를 구현한 서비스이어야 한다.
		auth.userDetailsService(daangnMemberService).passwordEncoder(new BCryptPasswordEncoder());
    }
	
	// BCrypt를 사용해 암호화 하겠다.
	// 암호화 객체 등록 
	@Bean(value = "passwordEncoder")
	BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
