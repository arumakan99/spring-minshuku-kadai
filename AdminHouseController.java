package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
// ルートパスの基準値を設定
// 今回だと、このコントローラ内の各メソッドが担当するURLはhttps://ドメイン名/admin/houses/○○
@RequestMapping("/admin/houses")
public class AdminHouseController {
	// 依存性の注入（DI）　今回はAdminHouseControllerがhouseRepositoryに依存
	// 依存先のオブジェクトをfinal宣言することで、一度初期化された後は変更されない
	private final HouseRepository houseRepository;
    private final HouseService houseService;    
	
	// コンストラクタ定義：HouseRepositoryのインスタンスを受け取ってhouseRepositoryに設定
    public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
        this.houseRepository = houseRepository; 
        this.houseService = houseService;   
    }	
	
	// /admin/housesがそのままマッピングされる
	@GetMapping
	// Pageable型の引数を設定することで、SB側で適切なPageableオブジェクトを生成
	// Pageable型の引数に@PageableDefaultをつけることでPageableオブジェクトが持つページ情報のデフォルト値を任意に設定できる
	// @RequestParamをつけることで、フォームから送信されたパラメータをその引数にバインドすることができる
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
//		// houseRepositoryから全てのHouseデータを取得しhousesに代入
//		List<House> houses = houseRepository.findAll();
		Page<House> housePage;
		
		// keywordパラメータが存在する場合は部分一致検索を行い、そうでなければ通常通り全権のデータを取得
		if(keyword != null && !keyword.isEmpty()) {
			// %をつけて部分一致検索
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			housePage = houseRepository.findAll(pageable);
		}
		
		// Modelクラスによりコントローラからビューにデータを渡す
		// model.addAttribute(ビュー側から参照する変数名, ビューに渡すデータ)
		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		
		// ビューを表示
		return "admin/houses/index";
	}

	// URLの一部をその引数にバインド
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		// URLのidと一致する民宿データを1つだけ取得
		// @PathVariableで引数idにバインドした値をgetReferenceById()メソッドの引数として渡す
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		
		return "admin/houses/show";
	}
	
	// フォームクラス利用には、コントローラからフォームを表示するビューにそのインスタンスを渡す
	// フォームの各入力項目とフォームクラスの各フィールドをバインドするため
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm());
		return "admin/houses/register";
	}
	
    @PostMapping("/create")
    // @ModelAttribute：フォームから送信されたデータをその引数にバインドする
    // BindingResult：バリデーションのエラー内容がその引数に格納される
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
    	// エラーが存在する場合は民宿登録ページを表示
    	if (bindingResult.hasErrors()) {
            return "admin/houses/register";
        }
        
    	// create()で民宿を登録する
        houseService.create(houseRegisterForm);
        // redirectAttributeインターフェースが提供するaddFlashAttribute()：リダイレクト先にデータを渡す
        	// リダイレクト先で取得されたあと自動的に削除される（リダイレクト直度に1回限り利用するデータを渡す時に使う）
        // addFlashAttribute(リダイレクト先から参照する変数名, リダイレクト先に渡すデータ)
        redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");    
        
        // ビューを呼び出すのではなくリダイレクトさせたい場合は"redirect:ルートパス"と記述
        // 今回は民宿一覧ページにリダイレクトさせる
        return "redirect:/admin/houses";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id);
        String imageName = house.getImageName();
        // フォームクラスをインスタンス化
        HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(), house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(), house.getPhoneNumber());
        
        // インスタンスをビューに渡す
        model.addAttribute("imageName", imageName);
        model.addAttribute("houseEditForm", houseEditForm);
        
        return "admin/houses/edit";
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/houses/edit";
        }
        
        houseService.update(houseEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
        
        return "redirect:/admin/houses";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
    	houseRepository.deleteById(id);
    	
    	redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
    	
    	return "redirect:/admin/houses";
    }
}
