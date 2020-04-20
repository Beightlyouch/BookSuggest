package com.beightlyouch.booksuggest;

public class BookCenter {
    public static String genre[] = {
            //よくわからんけどエラーのジャンルもある　やばい
            "001001",    //漫画（コミック）
            "001002",    //語学・学習参考書
            "001003",    //絵本・児童書・図鑑
            "001004",    //小説・エッセイ
            "001005",    //パソコン・システム開発
            "001006",    //ビジネス・経済・就職
            "001007",    //旅行・留学・アウトドア
            "001008",    //人文・思想・社会
            "001009",    //ホビー・スポーツ・美術
            "001010",    //美容・暮らし・健康・料理
            "001011",    //エンタメ・ゲーム
            "001012",    //科学・医学・技術
            "001013",    //写真集・タレント
            "001015",    //その他
            "001016",    //資格・検定
            "001017",    //ライトノベル
            "001018",    //楽譜
            "001019",    //文庫
            "001020",    //新書
            "001021",    //ボーイズラブ（BL）
            "001022",    //付録付き
            "001001",  //漫画（コミック）
            "001001001",    //少年
            "001001002",    //少女
            "001001003",    //青年
            "001001004",    //レディース
            "001001006",    //文庫
            "001001012",    //その他
            //    001002      //語学・学習参考書
            "001002001",    //語学学習
            "001002002",    //語学辞書
            "001002003",    //辞典
            "001002005",    //語学関係資格
            "001002006",    //学習参考書・問題集
            "001002007",    //その他
            //     001003      //絵本・児童書・図鑑
            "001003001",    //児童書
            "001003002",    //児童文庫
            "001003003",    //絵本
            "001003004",    //民話・むかし話
            "001003005",    //しかけ絵本
            "001003006",    //図鑑・ちしき
            "001003007",    //その他
            // 001004      //小説・エッセイ
            "001004001",    //ミステリー・サスペンス
            "001004002",    //SF・ホラー
            "001004003",    //エッセイ
            "001004004",    //ノンフィクション
            "001004008",    //日本の小説
            "001004009",    //外国の小説
            "001004015",    //その他
            "001004016",    //ロマンス
            //     001005      //パソコン・システム開発
            "001005001",    //ハードウェア
            "001005002",    //入門書
            "001005003",    //インターネット・WEBデザイン
            "001005004",    //ネットワーク
            "001005005",    //プログラミング
            "001005006",    //アプリケーション
            "001005007",    //OS
            "001005008",    //デザイン・グラフィックス
            "001005009",    //ITパスポート
            "001005010",    //MOUS・MOT
            "001005011",    //パソコン検定
            "001005013",    //IT・eコマース
            "001005017",    //その他
            // 001006      //ビジネス・経済・就職
            "001006001",    //経済・財政
            "001006002",    //流通
            "001006003",    //IT・eコマース
            "001006004",    //マーケティング・セールス
            "001006005",    //株・資金運用
            "001006006",    //マネープラン
            "001006007",    //マネジメント・人材管理
            "001006008",    //経理
            "001006009",    //自己啓発
            "001006010",    //就職・転職
            "001006011",    //MBA
            "001006012",    //CPA(米国公認会計士）
            "001006013",    //証券アナリスト
            "001006014",    //税理士・公認会計士・ファイナンシャルプランナー
            "001006015",    //簿記検定
            "001006016",    //銀行・金融検定
            "001006018",    //経営
            "001006019",    //産業
            "001006021",    //その他
            "001006022",    //アフィリエイト
            "001006023",    //ビジネスマナー
            //001007      //旅行・留学・アウトドア
            "001007001",    //旅行
            "001007003",    //温泉
            "001007004",    //鉄道の旅
            "001007005",    //テーマパーク
            "001007006",    //ガイドブック
            "001007007",    //地図
            "001007008",    //留学・海外赴任
            "001007010",    //旅行主任者
            "001007011",    //紀行・旅行エッセイ
            "001007012",    //釣り
            "001007013",    //その他
            "001007014",    //キャンプ
            "001008",      //人文・思想・社会
            "001008001",    //雑学・出版・ジャーナリズム
            "001008002",    //哲学・思想
            "001008003",    //心理学
            "001008004",    //宗教・倫理
            "001008005",    //歴史
            "001008006",    //地理
            "001008008",    //社会科学
            "001008009",    //法律
            "001008010",    //政治
            "001008011",    //社会
            "001008012",    //教育・福祉
            "001008015",    //民俗
            "001008016",    //軍事
            "001008017",    //ノンフィクション
            "001008018",    //言語学
            "001008019",    //語学学習
            "001008020",    //語学辞書
            "001008022",    //文学
            "001008027",    //その他
            //001009      //ホビー・スポーツ・美術
            "001009001",    //スポーツ
            "001009002",    //格闘技
            "001009003",    //車・バイク
            "001009004",    //鉄道
            "001009005",    //登山・アウトドア・釣り
            "001009006",    //カメラ・写真
            "001009007",    //囲碁・将棋・クイズ
            "001009008",    //ギャンブル
            "001009009",    //美術
            "001009010",    //工芸・工作
            "001009011",    //茶道・香道・華道
            "001009012",    //ミリタリー
            "001009013",    //トレーディングカード
            "001009014",    //その他
            "001009015",    //自転車
            "001010",      //美容・暮らし・健康・料理
            "001010001",    //恋愛
            "001010002",    //妊娠・出産・子育て
            "001010003",    //ペット
            "001010004",    //住まい・インテリア
            "001010005",    //ガーデニング・フラワー
            "001010006",    //生活の知識
            "001010007",    //占い
            "001010008",    //冠婚葬祭・マナー
            "001010009",    //手芸
            "001010010",    //健康
            "001010011",    //料理
            "001010012",    //ドリンク・お酒
            "001010013",    //生き方・リラクゼーション
            "001010014",    //ファッション・美容
            "001010015",    //その他
            //"001011",      //げーむ
            "001011001",    //テレビ関連本
            "001011002",    //映画
            "001011003",    //音楽
            "001011005",    //ゲーム
            "001011006",    //演劇・舞踊
            "001011007",    ///演芸
            "001011009",    //アニメーション
            "001011010",    //フィギュア
            "001011011",    //サブカルチャー
            "001011012",    //その他
            "001011013",    //タレント関連本
            //"001012",    //科学・医学・技術
            "001012001",    //自然科学全般
            "001012002",    //数学
            "001012003",    //物理学
            "001012004",    //化学
            "001012005",    //地学・天文学
            "001012006",    //生物学
            "001012007",    //植物学
            "001012008",    //動物学
            "001012009",    //医学・薬学
            "001012010",    //工学
            "001012011",    //建築学
            "001012016",    //その他
            //"001013",    //写真集・タレント
            "001013001",    //グラビアアイドル・タレント写真集
            "001013002",    //その他
            "001015",      //その他
            //"001016",      //資格
            "001016001",    //看護・医療関係資格
            "001016002",    //法律関係資格
            "001016003",    //ビジネス関係資格
            "001016004",    //宅建・不動産関係資格
            "001016005",    //食品・調理関係資格
            "001016006",    //教育・心理関係資格
            "001016007",    //インテリア関係資格
            "001016008",    //介護・福祉関係資格
            "001016009",    //技術・建築関係資格
            "001016010",    //語学関係資格
            "001016011",    //パソコン関係資格
            "001016012",    //旅行主任者
            "001016013",    //カラーコーディネーター・色彩検定
            "001016014",    //数学検定
            "001016015",    //公務員試験
            "001016016",    //自動車免許
            "001016017",    //その他
            // "001017",      //ライトノベル
            "001017004",    //その他
            "001017005",    //少年
            "001017006",    //少女
            "001018",      //楽譜
            "001018001",    //全般
            // "001019",     //文庫
            "001019001",    //小説・エッセイ
            "001019002",    //美容・暮らし・健康・料理
            "001019003",    //ホビー・スポーツ・美術
            "001019005",    //語学・学習参考書
            "001019006",    //旅行・留学・アウトドア
            "001019007",    //人文・思想・社会
            "001019008",    //ビジネス・経済・就職
            "001019009",    //パソコン・システム開発
            "001019010",    //科学・医学・技術
            "001019011",    //漫画（コミック）
            "001019012",    //ライトノベル
            "001019013",    //エンタメ
            "001019014",    //写真集・タレント
            "001019015",    //その他
            //"001020",    //新書
            "001020001",    //小説・エッセイ
            "001020002",    //美容・暮らし・健康・料理
            "001020003",    //ホビー・スポーツ・美術
            "001020004",    //絵本・児童書・図鑑
            "001020005",    ///語学・学習参考書
            "001020006",    //旅行・留学・アウトドア
            "001020007",    //人文・思想・社会
            "001020008",    //ビジネス・経済・就職
            "001020009",    //パソコン・システム開発
            "001020010",    //科学・医学・技術
            "001020011",    //エンタメ
            "001020014",    //その他
            //001021      //BL
            "001021001",    //小説
            "001021002",    //コミック
            "001021003"
    };
}