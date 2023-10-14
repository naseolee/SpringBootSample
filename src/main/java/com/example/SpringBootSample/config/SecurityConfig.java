package com.example.SpringBootSample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


// Security設定クラスを用意する為に以下のアノテーションの指定と継承を行う
// 今はdeprecatedになっているが、オバーライドすることでセクシュアリティ設定を変更することができるよう
@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
// 何でdeprecated?
// p395は想定通りにならない(共通エラー画面が表示)★これうまく行けないけど一旦進む
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	// Spring セキュリティを使うためにはパスワードを暗号化する必要がある
	@Bean
	public PasswordEncoder passwordEncoder() {
		// 以下は推奨されている
		return new BCryptPasswordEncoder();
	}
	
	/** セキュリティの対象外を設定 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		//　セキュリティを適用しない
		// web.ignoring().antMatchers("除外対象のパス")
		web
			.ignoring()
				.antMatchers("/webjars/**")
				.antMatchers("/css/**")
				.antMatchers("/js/**")
				.antMatchers("/h2-console/**");
		
		// TODO: 共通エラー画面にも行けなくなったけどどうすればいい？
	}
	
	/** セキュリティの各種設定 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// ログイン不要ページの設定
		http
			.authorizeRequests()
				//特定のパス
				.antMatchers("/login", "/user/signup").permitAll() //直リンクOK
				.antMatchers("/user/signup/rest").permitAll() // 12章のRESTで追加
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN") // 権限制御=権限もっているユーザーはアクセス可能
				//全てのパスこれを最初に設定すると全てのリンクに認証が必要になるため、メソッドチェインの順番に気を付ける
				.anyRequest().authenticated(); // それ以外は直リンクNG
		
		//// ログイン処理をできるように修正
		http
			// このメソッドを呼び出してメソッドチェーインで条件を追加する。
			.formLogin()
				.loginProcessingUrl("/login") // ログイン処理のパス　formタグのth:actionに指定したパスに一致させる
				.loginPage("/login") //　ログインページの指定(@GetMappingのパスとこれを一致させる)
				.failureUrl("/login?error") // ログイン失敗時の遷移先(?の後ろは何でもいいような？)
				.usernameParameter("userId") // ログインページのユーザーID()
				.passwordParameter("password") // ログインページのパスワード(Htmlのパスワード覧のname属性を設定)
				.defaultSuccessUrl("/user/list", true); // 成功後の遷移先(2引数は第一引数に強制的に遷移させる)
		
		//　ログアウト処理
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Springの defaultではログアウト処理は
																			// POSTメソッドで送る。
																			//　もしGetでログアウトリクエストを送る場合はこのメソッドを追加する!!
				.logoutUrl("/logout") // ログアウトのリクエスト先パスを設定する。　HTMLの「th:action="@{/logout}"」のパスと一致させる・Postメソッドでログアウトする場合設定する
				.logoutSuccessUrl("/login?logout"); //　ログアウト成功時の遷移先
				// この設定をするとLogOutControllerが不要になる。削除しても動作する
		
		//　クロスサイトリクエストフォージェリ(Cross Site Request Forgeries)の頭文字を取って、CSRFという438p
		// Springではこの攻撃を防ぐためにトークンが含まれるようにする。トークンはランダムな文字列のこと
		// トークンはGET以外のメソッドで必要になる、GETメソッドには含める必要がない
		// SpringでsecurityではCSRFがデフォルトで有効になっている。
		// CSRF対策を無効に設定(練習用で一時的無効にする)
		//http.csrf().disable();
		
		

 
		
	}
	
	/** 認証の設定 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//インメモリ用のユーザーパスワードを暗号化する
		PasswordEncoder encoder = passwordEncoder();
		
		/* ユーザーデータで認証を実装したため使わない
		// インメモリ認証(開発用)
		auth
			.inMemoryAuthentication()
				.withUser("user") //userを追加
				.password(encoder.encode("user"))
				.roles("GENERAL")
			.and() // 複数作ることもできる
				.withUser("admin") // adminを追加
				.password(encoder.encode("admin"))
				.roles("ADMIN");
		*/
		
		// ユーザーデータで認証
		// 自作のUserDetailsServiceを設定する
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder);
	}
	
}
