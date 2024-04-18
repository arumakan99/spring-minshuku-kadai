// imageFileというIDを持つ要素を取得しimageInputという定数に代入
const imageInput = document.getElementById('imageFile');
// imagePreviewというIDを持つ要素を取得しimagePreviewという定数に代入
const imagePreview = document.getElementById('imagePreview');

// imageInput要素に変化があった場合ファイルの読み込みとプレビュー表示の処理を行う
imageInput.addEventListener('change', () => {
	// 選択されたファイルがあるかどうか確認する
	if (imageInput.files[0]) {
		// FileReaderオブジェクトを作成		
		let fileReader = new FileReader();
		// ファイルの読み込みが完了した際に実行されるコールバック関数を指定
		fileReader.onload = () => {
			imagePreview.innerHTML = `<img src="${fileReader.result}" class="mb-3">`;
		}
		// 読み込んだ画像ファイルを画像プレビュー用の要素に表示
		fileReader.readAsDataURL(imageInput.files[0]);
	} else {
		// 選択されたファイルがない場合は画像プレビュー用の要素を空にする
		imagePreview.innerHTML = '';
	}
})