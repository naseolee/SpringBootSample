/**
 * jQueryを使用すると、$.ajax関数を使ってajax通信ができます。ajax通信とは、非同期通信のことです。
 * 例えば、ファイルダウンロードなど
 * 注意点：CSRFを使用している場合、トークンも送信する必要がある。ここではformの内容を全て送っているため、送信内容に含まれる
 * 画面を作る際にはリアクションを付けるようにする。(ボタンを押した時に成功したか、失敗したかの確認メッセージを表示するのか一般的)
 */
'user strict'

/** 画面ロード時の処理 */
jQuery(function($) {
	/** 更新ボタンを押した時の処理 */
	$('#btn-update').click(function(event) {
		//ユーザー更新
		updateUser();
	});
	
	/** 削除ボタンを押したときの処理 */
	$('#btn-delete').click(function(event) {
		//ユーザー削除
		deleteUser();
	});

	/** ユーザ更新処理 */
	function updateUser() {
		// フォームの値を取得
		var formData = $('#user-detail-form').serializeArray();
		
		// ajax通信
		$.ajax({
			type:"PUT",
			cache: false,
			url:'/user/update',
			data:formData,
			dataType:'json',
			
		}).done(function(data) {
			// ajax成功時処理
			alert('ユーザーを更新しました');
			// ユーザー一覧画面にリダイレクト
			window.location.href='/user/list';
			
		}).fail(function(jqXHR, textStatus, errorThrown) {
			// ajax失敗時処理
			alert('ユーザーを更新に失敗しました');
			
		}).always(function(){
			// 常に実行する処理
		});
	}
	
		/** ユーザ更新処理 */
	function deleteUser() {
		// フォームの値を取得
		var formData = $('#user-detail-form').serializeArray();
		
				// ajax通信
		$.ajax({
			type:"DELETE",
			cache: false,
			url:'/user/delete',
			data:formData,
			dataType:'json',
			
		}).done(function(data) {
			// ajax成功時処理
			alert('ユーザーを削除しました');
			// ユーザー一覧画面にリダイレクト
			window.location.href='/user/list';
			
		}).fail(function(jqXHR, textStatus, errorThrown) {
			// ajax失敗時処理
			alert('ユーザーを削除に失敗しました');
			
		}).always(function(){
			// 常に実行する処理
		});
	}
});

