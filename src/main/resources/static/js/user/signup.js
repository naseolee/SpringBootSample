/**
 * 
 */
'user strict'

/** 画面ロード時の処理 */
jQuery(function($) {
	/** 登録ボタンを押した時の処理 */
	$('#btn-signup').click(function(event) {
		//ユーザー登録
		signupUser();
	});
	
	/** ユーザ登録処理 */
	function signupUser() {
		
		// バリデーション結果をクリア
		removeValidResult();
		
		// フォームの値を取得
		var formData = $('#signup-form').serializeArray();
		
		// ajax通信
		$.ajax({
			type:"POST",
			cache: false,
			url:'/user/signup/rest',
			data:formData,
			dataType:'json',
			
		}).done(function(data) {
			//// doneの中のdata変数には実行結果が入られている(ここではRestResult)
			// ajax成功時処理
			console.log(data); //　デバッグ時のみ使用、実際にはbrowserのデバッグを使用すること
			
			if(data.result === 90) {
				//// $.eachは繰り返し処理をするための関数、第一引数に繰り返し処理をする対象が入る
				//// data.errorsは RestResultのerrorsフィールドのこと
				////　errorsはmapであるため、第二引数の部分でerrorsフィールド内のkeyとvalueを取り出している
				// validationエラー時の処理
				$.each(data.errors, function(key, value) {
					reflectValidResult(key, value)
				});
				
			} else if(data.result === 0) {
				alert('ユーザーを登録しました');
				// ユーザー一覧画面にリダイレクト
				window.location.href='/login';
			}
			
		}).fail(function(jqXHR, textStatus, errorThrown) {
			// ajax失敗時処理
			alert('ユーザーを登録に失敗しました');
			
		}).always(function(){
			// 常に実行する処理
		});
	}
	
	/** バリデーション結果クリア */
	function removeValidResult() {
		$('.is-invalid').removeClass('is-invalid');
		$('.invalid-feedback').remove();
		$('.text-danger').remove();
	}
	
	/** バリデーション結果の繁栄 */
	function reflectValidResult(key, value) {
		
		// エラーメッセージを追加
		if(key === 'gender') { // 性別の場合
			// CSS適用
			$('input[name='+key+']').addClass('is-invalid');
			//エラーメッセージ追加
			$('input[name='+key+']')
			.parent().parent()
			.append('<div class="text-danger">' + value + '</div>');
			
		} else { // 性別以外の場合
			// CSS適用
			$('input[name='+key+']').addClass('is-invalid');
			//エラーメッセージ追加
			$('input[name='+key+']')
			.after('<div class="invalid-feedback">' + value + '</div>');
			
		}
	}
});

