package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

// サービスクラスとして機能させる
@Service
public class HouseService {
	// HouseRepositoryクラスのインスタンスを定義
	private final HouseRepository houseRepository;    

	// コンストラクタ
	// HouseRepositoryを依存性の注入によってインスタンス化
	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;        
	}    

	// メソッドをトランザクション（DB操作をひとまとまりにしたもの）化
	@Transactional
	// create()：データの登録処理
	public void create(HouseRegisterForm houseRegisterForm) {
		// エンティティ（Houseクラス）をインスタンス化
		House house = new House();    
		// 画像ファイルを取得
		MultipartFile imageFile = houseRegisterForm.getImageFile();

		// 画像ファイルが空でない場合の処理
		if (!imageFile.isEmpty()) {
			// getOriginalFilename()：元のファイル名を取得する
			String imageName = imageFile.getOriginalFilename(); 
			String hashedImageName = generateNewFileName(imageName);
			// Pathクラス：特定のファイルやディレクトリのパスを表現する
			// Path filePathでファイルの保存先パスを作成
			// Pathsクラス：Pathクラスのインスタンス生成を補助する
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			// メソッドを呼び出して画像ファイルを保存
			// imageFileの内容をfilePathにコピー
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}

		// フォームの各入力項目の値を取得し、セッターを使ってインスタンス化したhouseエンティティの各フィールドにセット
		house.setName(houseRegisterForm.getName());                
		house.setDescription(houseRegisterForm.getDescription());
		house.setPrice(houseRegisterForm.getPrice());
		house.setCapacity(houseRegisterForm.getCapacity());
		house.setPostalCode(houseRegisterForm.getPostalCode());
		house.setAddress(houseRegisterForm.getAddress());
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

		// save()：エンティティをDBに保存
		houseRepository.save(house);
	}
	
    @Transactional
    public void update(HouseEditForm houseEditForm) {
        House house = houseRepository.getReferenceById(houseEditForm.getId());
        MultipartFile imageFile = houseEditForm.getImageFile();
        
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); 
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            house.setImageName(hashedImageName);
        }
        
        house.setName(houseEditForm.getName());                
        house.setDescription(houseEditForm.getDescription());
        house.setPrice(houseEditForm.getPrice());
        house.setCapacity(houseEditForm.getCapacity());
        house.setPostalCode(houseEditForm.getPostalCode());
        house.setAddress(houseEditForm.getAddress());
        house.setPhoneNumber(houseEditForm.getPhoneNumber());
                    
        houseRepository.save(house);
    } 

	// UUIDを使ってファイル名を別名に変更
	// generateNewFileName()：ファイル名の変更処理
	public String generateNewFileName(String fileName) {
		// split()：文字列を指定した区切り文字で分割する
		// ファイル名を分割した配列を作成
		String[] fileNames = fileName.split("\\.");   
		// length()：文字列の文字数を取得する
		// ファイル名の各部分にUUIDを割り当てる
		for (int i = 0; i < fileNames.length - 1; i++) {
			// UUIDクラス：UUID（ほぼ重複しない一意のID）を表現するためのクラス
			// randomUUID()：UUIDを取得する
			fileNames[i] = UUID.randomUUID().toString();            
		}
		// join()：指定した区切り文字で複数の文字列を結合する
		// UUIDを含む新しいファイル名を作成
		String hashedFileName = String.join(".", fileNames);
		// 新しいファイル名を返す
		return hashedFileName;
	}     

	// 画像ファイルを指定したファイルにコピーする
	// copyImageFile()：ファイルのコピー処理
	public void copyImageFile(MultipartFile imageFile, Path filePath) {           
		try {
			// get()：指定したパスをPathクラスのインスタンスに変換する
			// Filesクラス：ファイルやディレクトリを簡単かつ効率的に操作する
			// copy()：第1引数に指定したファイルを第2引数に指定したパスにコピーする
			// InputStreamクラス：バイト（byte）の入力ストリームを表現するための抽象クラス
			// getInputStream()：ファイルの内容を読み取るためのInputStreamオブジェクトを取得する
			// 画像ファイルを指定したパスにコピー
			Files.copy(imageFile.getInputStream(), filePath);
		// 例外処理　エラーが発生した場合はスタックトレースを表示
		} catch (IOException e) {
			e.printStackTrace();
		}          
	} 
}
