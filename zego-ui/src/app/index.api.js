(function ()
{
    'use strict';

    angular
        .module('fuse')
        .factory('api', apiService);

    /** @ngInject */
    function apiService($resource,$cookies,$rootScope,$q,$http,$mdDialog)
    {
        /**
         * You can use this service to define your API urls. The "api" service
         * is designed to work in parallel with "apiResolver" service which you can
         * find in the "app/core/services/api-resolver.service.js" file.
         *
         * You can structure your API urls whatever the way you want to structure them.
         * You can either use very simple definitions, or you can use meulti-dimensional
         * objects.
         *
         * Here's a very simple API url definition example:
         *
         *      api.getBlogList = $resource('http://api.example.com/getBlogList');
         *
         * While this is a perfectly valid $resource definition, most of the time you will
         * find yourself in a more complex situation where you want url parameters:
         *
         *      api.getBlogById = $resource('http://api.example.com/blog/:id', {id: '@id'});
         *
         * You can also define your custom methods. Custom method definitions allow you to
         * add hardcoded parameters to your API calls that you want to sent every time you
         * make that API call:
         *
         *      api.getBlogById = $resource('http://api.example.com/blog/:id', {id: '@id'}, {
         *         'getFromHomeCategory' : {method: 'GET', params: {blogCategory: 'home'}}
         *      });
         *
         * In addition to these definitions, you can also create multi-dimensional objects.
         * They are nothing to do with the $resource object, it's just a more convenient
         * way that we have created for you to packing your related API urls together:
         *
         *      api.blog = {
         *                   list     : $resource('http://api.example.com/blog'),
         *                   getById  : $resource('http://api.example.com/blog/:id', {id: '@id'}),
         *                   getByDate: $resource('http://api.example.com/blog/:date', {id: '@date'}, {
         *                       get: {
         *                            method: 'GET',
         *                            params: {
         *                                getByDate: true
         *                            }
         *                       }
         *                   })
         *       }
         *
         * If you look at the last example from above, we overrode the 'get' method to put a
         * hardcoded parameter. Now every time we make the "getByDate" call, the {getByDate: true}
         * object will also be sent along with whatever data we are sending.
         *
         * All the above methods are using standard $resource service. You can learn more about
         * it at: https://docs.angularjs.org/api/ngResource/service/$resource
         *
         * -----
         *
         * After you defined your API urls, you can use them in Controllers, Services and even
         * in the UIRouter state definitions.
         *
         * If we use the last example from above, you can do an API call in your Controllers and
         * Services like this:
         *
         *      function MyController (api)
         *      {
         *          // Get the blog list
         *          api.blog.list.get({},
         *
         *              // Success
         *              function (response)
         *              {
         *                  console.log(response);
         *              },
         *
         *              // Error
         *              function (response)
         *              {
         *                  console.error(response);
         *              }
         *          );
         *
         *          // Get the blog with the id of 3
         *          var id = 3;
         *          api.blog.getById.get({'id': id},
         *
         *              // Success
         *              function (response)
         *              {
         *                  console.log(response);
         *              },
         *
         *              // Error
         *              function (response)
         *              {
         *                  console.error(response);
         *              }
         *          );
         *
         *          // Get the blog with the date by using custom defined method
         *          var date = 112314232132;
         *          api.blog.getByDate.get({'date': date},
         *
         *              // Success
         *              function (response)
         *              {
         *                  console.log(response);
         *              },
         *
         *              // Error
         *              function (response)
         *              {
         *                  console.error(response);
         *              }
         *          );
         *      }
         *
         * Because we are directly using $resource service, all your API calls will return a
         * $promise object.
         *
         * --
         *
         * If you want to do the same calls in your UI Router state definitions, you need to use
         * "apiResolver" service we have prepared for you:
         *
         *      $stateProvider.state('app.blog', {
         *          url      : '/blog',
         *          views    : {
         *               'content@app': {
         *                   templateUrl: 'app/main/apps/blog/blog.html',
         *                   controller : 'BlogController as vm'
         *               }
         *          },
         *          resolve  : {
         *              Blog: function (apiResolver)
         *              {
         *                  return apiResolver.resolve('blog.list@get');
         *              }
         *          }
         *      });
         *
         *  You can even use parameters with apiResolver service:
         *
         *      $stateProvider.state('app.blog.show', {
         *          url      : '/blog/:id',
         *          views    : {
         *               'content@app': {
         *                   templateUrl: 'app/main/apps/blog/blog.html',
         *                   controller : 'BlogController as vm'
         *               }
         *          },
         *          resolve  : {
         *              Blog: function (apiResolver, $stateParams)
         *              {
         *                  return apiResolver.resolve('blog.getById@get', {'id': $stateParams.id);
         *              }
         *          }
         *      });
         *
         *  And the "Blog" object will be available in your BlogController:
         *
         *      function BlogController(Blog)
         *      {
         *          var vm = this;
         *
         *          // Data
         *          vm.blog = Blog;
         *
         *          ...
         *      }
         */
         var langIso = [
     {"isocode":"ab","lang":"Abkhaz","nativeName":"аҧсуа"},
     {"isocode":"aa","lang":"Afar","nativeName":"Afaraf"},
     {"isocode":"af","lang":"Afrikaans","nativeName":"Afrikaans"},
     {"isocode":"ak","lang":"Akan","nativeName":"Akan"},
     {"isocode":"sq","lang":"Albanian","nativeName":"Shqip"},
     {"isocode":"am","lang":"Amharic","nativeName":"አማርኛ"},
     {"isocode":"ar","lang":"Arabic","nativeName":"العربية"},
     {"isocode":"an","lang":"Aragonese","nativeName":"Aragonés"},
     {"isocode":"hy","lang":"Armenian","nativeName":"Հայերեն"},
     {"isocode":"as","lang":"Assamese","nativeName":"অসমীয়া"},
     {"isocode":"av","lang":"Avaric","nativeName":"авар мацӀ, магӀарул мацӀ"},
     {"isocode":"ae","lang":"Avestan","nativeName":"avesta"},
     {"isocode":"ay","lang":"Aymara","nativeName":"aymar aru"},
     {"isocode":"az","lang":"Azerbaijani","nativeName":"azərbaycan dili"},
     {"isocode":"bm","lang":"Bambara","nativeName":"bamanankan"},
     {"isocode":"ba","lang":"Bashkir","nativeName":"башҡорт теле"},
     {"isocode":"eu","lang":"Basque","nativeName":"euskara, euskera"},
     {"isocode":"be","lang":"Belarusian","nativeName":"Беларуская"},
     {"isocode":"bn","lang":"Bengali","nativeName":"বাংলা"},
     {"isocode":"bh","lang":"Bihari","nativeName":"भोजपुरी"},
     {"isocode":"bi","lang":"Bislama","nativeName":"Bislama"},
     {"isocode":"bs","lang":"Bosnian","nativeName":"bosanski jezik"},
     {"isocode":"br","lang":"Breton","nativeName":"brezhoneg"},
     {"isocode":"bg","lang":"Bulgarian","nativeName":"български език"},
     {"isocode":"my","lang":"Burmese","nativeName":"ဗမာစာ"},
     {"isocode":"ca","lang":"Catalan; Valencian","nativeName":"Català"},
     {"isocode":"ch","lang":"Chamorro","nativeName":"Chamoru"},
     {"isocode":"ce","lang":"Chechen","nativeName":"нохчийн мотт"},
     {"isocode":"ny","lang":"Chichewa; Chewa; Nyanja","nativeName":"chiCheŵa, chinyanja"},
     {"isocode":"zh","lang":"Chinese","nativeName":"中文 (Zhōngwén), 汉语, 漢語"},
     {"isocode":"cv","lang":"Chuvash","nativeName":"чӑваш чӗлхи"},
     {"isocode":"kw","lang":"Cornish","nativeName":"Kernewek"},
     {"isocode":"co","lang":"Corsican","nativeName":"corsu, lingua corsa"},
     {"isocode":"cr","lang":"Cree","nativeName":"ᓀᐦᐃᔭᐍᐏᐣ"},
     {"isocode":"hr","lang":"Croatian","nativeName":"hrvatski"},
     {"isocode":"cs","lang":"Czech","nativeName":"česky, čeština"},
     {"isocode":"da","lang":"Danish","nativeName":"dansk"},
     {"isocode":"dv","lang":"Divehi; Dhivehi; Maldivian;","nativeName":"ދިވެހި"},
     {"isocode":"nl","lang":"Dutch","nativeName":"Nederlands, Vlaams"},
     {"isocode":"en","lang":"English","nativeName":"English"},
     {"isocode":"eo","lang":"Esperanto","nativeName":"Esperanto"},
     {"isocode":"et","lang":"Estonian","nativeName":"eesti, eesti keel"},
     {"isocode":"ee","lang":"Ewe","nativeName":"Eʋegbe"},
     {"isocode":"fo","lang":"Faroese","nativeName":"føroyskt"},
     {"isocode":"fj","lang":"Fijian","nativeName":"vosa Vakaviti"},
     {"isocode":"fi","lang":"Finnish","nativeName":"suomi, suomen kieli"},
     {"isocode":"fr","lang":"French","nativeName":"français, langue française"},
     {"isocode":"ff","lang":"Fula; Fulah; Pulaar; Pular","nativeName":"Fulfulde, Pulaar, Pular"},
     {"isocode":"gl","lang":"Galician","nativeName":"Galego"},
     {"isocode":"ka","lang":"Georgian","nativeName":"ქართული"},
     {"isocode":"de","lang":"German","nativeName":"Deutsch"},
     {"isocode":"el","lang":"Greek, Modern","nativeName":"Ελληνικά"},
     {"isocode":"gn","lang":"Guaraní","nativeName":"Avañeẽ"},
     {"isocode":"gu","lang":"Gujarati","nativeName":"ગુજરાતી"},
     {"isocode":"ht","lang":"Haitian; Haitian Creole","nativeName":"Kreyòl ayisyen"},
     {"isocode":"ha","lang":"Hausa","nativeName":"Hausa, هَوُسَ"},
     {"isocode":"he","lang":"Hebrew (modern)","nativeName":"עברית"},
     {"isocode":"hz","lang":"Herero","nativeName":"Otjiherero"},
     {"isocode":"hi","lang":"Hindi","nativeName":"हिन्दी, हिंदी"},
     {"isocode":"ho","lang":"Hiri Motu","nativeName":"Hiri Motu"},
     {"isocode":"hu","lang":"Hungarian","nativeName":"Magyar"},
     {"isocode":"ia","lang":"Interlingua","nativeName":"Interlingua"},
     {"isocode":"id","lang":"Indonesian","nativeName":"Bahasa Indonesia"},
     {"isocode":"ie","lang":"Interlingue","nativeName":"Originally called Occidental; then Interlingue after WWII"},
     {"isocode":"ga","lang":"Irish","nativeName":"Gaeilge"},
     {"isocode":"ig","lang":"Igbo","nativeName":"Asụsụ Igbo"},
     {"isocode":"ik","lang":"Inupiaq","nativeName":"Iñupiaq, Iñupiatun"},
     {"isocode":"io","lang":"Ido","nativeName":"Ido"},
     {"isocode":"is","lang":"Icelandic","nativeName":"Íslenska"},
     {"isocode":"it","lang":"Italian","nativeName":"Italiano"},
     {"isocode":"iu","lang":"Inuktitut","nativeName":"ᐃᓄᒃᑎᑐᑦ"},
     {"isocode":"ja","lang":"Japanese","nativeName":"日本語 (にほんご／にっぽんご)"},
     {"isocode":"jv","lang":"Javanese","nativeName":"basa Jawa"},
     {"isocode":"kl","lang":"Kalaallisut, Greenlandic","nativeName":"kalaallisut, kalaallit oqaasii"},
     {"isocode":"kn","lang":"Kannada","nativeName":"ಕನ್ನಡ"},
     {"isocode":"kr","lang":"Kanuri","nativeName":"Kanuri"},
     {"isocode":"ks","lang":"Kashmiri","nativeName":"कश्मीरी, كشميري‎"},
     {"isocode":"kk","lang":"Kazakh","nativeName":"Қазақ тілі"},
     {"isocode":"km","lang":"Khmer","nativeName":"ភាសាខ្មែរ"},
     {"isocode":"ki","lang":"Kikuyu, Gikuyu","nativeName":"Gĩkũyũ"},
     {"isocode":"rw","lang":"Kinyarwanda","nativeName":"Ikinyarwanda"},
     {"isocode":"ky","lang":"Kirghiz, Kyrgyz","nativeName":"кыргыз тили"},
     {"isocode":"kv","lang":"Komi","nativeName":"коми кыв"},
     {"isocode":"kg","lang":"Kongo","nativeName":"KiKongo"},
     {"isocode":"ko","lang":"Korean","nativeName":"한국어 (韓國語), 조선말 (朝鮮語)"},
     {"isocode":"ku","lang":"Kurdish","nativeName":"Kurdî, كوردی‎"},
     {"isocode":"kj","lang":"Kwanyama, Kuanyama","nativeName":"Kuanyama"},
     {"isocode":"la","lang":"Latin","nativeName":"latine, lingua latina"},
     {"isocode":"lb","lang":"Luxembourgish, Letzeburgesch","nativeName":"Lëtzebuergesch"},
     {"isocode":"lg","lang":"Luganda","nativeName":"Luganda"},
     {"isocode":"li","lang":"Limburgish, Limburgan, Limburger","nativeName":"Limburgs"},
     {"isocode":"ln","lang":"Lingala","nativeName":"Lingála"},
     {"isocode":"lo","lang":"Lao","nativeName":"ພາສາລາວ"},
     {"isocode":"lt","lang":"Lithuanian","nativeName":"lietuvių kalba"},
     {"isocode":"lu","lang":"Luba-Katanga","nativeName":""},
     {"isocode":"lv","lang":"Latvian","nativeName":"latviešu valoda"},
     {"isocode":"gv","lang":"Manx","nativeName":"Gaelg, Gailck"},
     {"isocode":"mk","lang":"Macedonian","nativeName":"македонски јазик"},
     {"isocode":"mg","lang":"Malagasy","nativeName":"Malagasy fiteny"},
     {"isocode":"ms","lang":"Malay","nativeName":"bahasa Melayu, بهاس ملايو‎"},
     {"isocode":"ml","lang":"Malayalam","nativeName":"മലയാളം"},
     {"isocode":"mt","lang":"Maltese","nativeName":"Malti"},
     {"isocode":"mi","lang":"Māori","nativeName":"te reo Māori"},
     {"isocode":"mr","lang":"Marathi (Marāṭhī)","nativeName":"मराठी"},
     {"isocode":"mh","lang":"Marshallese","nativeName":"Kajin M̧ajeļ"},
     {"isocode":"mn","lang":"Mongolian","nativeName":"монгол"},
     {"isocode":"na","lang":"Nauru","nativeName":"Ekakairũ Naoero"},
     {"isocode":"nv","lang":"Navajo, Navaho","nativeName":"Diné bizaad, Dinékʼehǰí"},
     {"isocode":"nb","lang":"Norwegian Bokmål","nativeName":"Norsk bokmål"},
     {"isocode":"nd","lang":"North Ndebele","nativeName":"isiNdebele"},
     {"isocode":"ne","lang":"Nepali","nativeName":"नेपाली"},
     {"isocode":"ng","lang":"Ndonga","nativeName":"Owambo"},
     {"isocode":"nn","lang":"Norwegian Nynorsk","nativeName":"Norsk nynorsk"},
     {"isocode":"no","lang":"Norwegian","nativeName":"Norsk"},
     {"isocode":"ii","lang":"Nuosu","nativeName":"ꆈꌠ꒿ Nuosuhxop"},
     {"isocode":"nr","lang":"South Ndebele","nativeName":"isiNdebele"},
     {"isocode":"oc","lang":"Occitan","nativeName":"Occitan"},
     {"isocode":"oj","lang":"Ojibwe, Ojibwa","nativeName":"ᐊᓂᔑᓈᐯᒧᐎᓐ"},
     {"isocode":"cu","lang":"Old Church Slavonic, Church Slavic, Church Slavonic, Old Bulgarian, Old Slavonic","nativeName":"ѩзыкъ словѣньскъ"},
     {"isocode":"om","lang":"Oromo","nativeName":"Afaan Oromoo"},
     {"isocode":"or","lang":"Oriya","nativeName":"ଓଡ଼ିଆ"},
     {"isocode":"os","lang":"Ossetian, Ossetic","nativeName":"ирон æвзаг"},
     {"isocode":"pa","lang":"Panjabi, Punjabi","nativeName":"ਪੰਜਾਬੀ, پنجابی‎"},
     {"isocode":"pi","lang":"Pāli","nativeName":"पाऴि"},
     {"isocode":"fa","lang":"Persian","nativeName":"فارسی"},
     {"isocode":"pl","lang":"Polish","nativeName":"polski"},
     {"isocode":"ps","lang":"Pashto, Pushto","nativeName":"پښتو"},
     {"isocode":"pt","lang":"Portuguese","nativeName":"Português"},
     {"isocode":"qu","lang":"Quechua","nativeName":"Runa Simi, Kichwa"},
     {"isocode":"rm","lang":"Romansh","nativeName":"rumantsch grischun"},
     {"isocode":"rn","lang":"Kirundi","nativeName":"kiRundi"},
     {"isocode":"ro","lang":"Romanian, Moldavian, Moldovan","nativeName":"română"},
     {"isocode":"ru","lang":"Russian","nativeName":"русский язык"},
     {"isocode":"sa","lang":"Sanskrit (Saṁskṛta)","nativeName":"संस्कृतम्"},
     {"isocode":"sc","lang":"Sardinian","nativeName":"sardu"},
     {"isocode":"sd","lang":"Sindhi","nativeName":"सिन्धी, سنڌي، سندھی‎"},
     {"isocode":"se","lang":"Northern Sami","nativeName":"Davvisámegiella"},
     {"isocode":"sm","lang":"Samoan","nativeName":"gagana faa Samoa"},
     {"isocode":"sg","lang":"Sango","nativeName":"yângâ tî sängö"},
     {"isocode":"sr","lang":"Serbian","nativeName":"српски језик"},
     {"isocode":"gd","lang":"Scottish Gaelic; Gaelic","nativeName":"Gàidhlig"},
     {"isocode":"sn","lang":"Shona","nativeName":"chiShona"},
     {"isocode":"si","lang":"Sinhala, Sinhalese","nativeName":"සිංහල"},
     {"isocode":"sk","lang":"Slovak","nativeName":"slovenčina"},
     {"isocode":"sl","lang":"Slovene","nativeName":"slovenščina"},
     {"isocode":"so","lang":"Somali","nativeName":"Soomaaliga, af Soomaali"},
     {"isocode":"st","lang":"Southern Sotho","nativeName":"Sesotho"},
     {"isocode":"es","lang":"Spanish; Castilian","nativeName":"español, castellano"},
     {"isocode":"su","lang":"Sundanese","nativeName":"Basa Sunda"},
     {"isocode":"sw","lang":"Swahili","nativeName":"Kiswahili"},
     {"isocode":"ss","lang":"Swati","nativeName":"SiSwati"},
     {"isocode":"sv","lang":"Swedish","nativeName":"svenska"},
     {"isocode":"ta","lang":"Tamil","nativeName":"தமிழ்"},
     {"isocode":"te","lang":"Telugu","nativeName":"తెలుగు"},
     {"isocode":"tg","lang":"Tajik","nativeName":"тоҷикӣ, toğikī, تاجیکی‎"},
     {"isocode":"th","lang":"Thai","nativeName":"ไทย"},
     {"isocode":"ti","lang":"Tigrinya","nativeName":"ትግርኛ"},
     {"isocode":"bo","lang":"Tibetan Standard, Tibetan, Central","nativeName":"བོད་ཡིག"},
     {"isocode":"tk","lang":"Turkmen","nativeName":"Türkmen, Түркмен"},
     {"isocode":"tl","lang":"Tagalog","nativeName":"Wikang Tagalog, ᜏᜒᜃᜅ᜔ ᜆᜄᜎᜓᜄ᜔"},
     {"isocode":"tn","lang":"Tswana","nativeName":"Setswana"},
     {"isocode":"to","lang":"Tonga (Tonga Islands)","nativeName":"faka Tonga"},
     {"isocode":"tr","lang":"Turkish","nativeName":"Türkçe"},
     {"isocode":"ts","lang":"Tsonga","nativeName":"Xitsonga"},
     {"isocode":"tt","lang":"Tatar","nativeName":"татарча, tatarça, تاتارچا‎"},
     {"isocode":"tw","lang":"Twi","nativeName":"Twi"},
     {"isocode":"ty","lang":"Tahitian","nativeName":"Reo Tahiti"},
     {"isocode":"ug","lang":"Uighur, Uyghur","nativeName":"Uyƣurqə, ئۇيغۇرچە‎"},
     {"isocode":"uk","lang":"Ukrainian","nativeName":"українська"},
     {"isocode":"ur","lang":"Urdu","nativeName":"اردو"},
     {"isocode":"uz","lang":"Uzbek","nativeName":"zbek, Ўзбек, أۇزبېك‎"},
     {"isocode":"ve","lang":"Venda","nativeName":"Tshivenḓa"},
     {"isocode":"vi","lang":"Vietnamese","nativeName":"Tiếng Việt"},
     {"isocode":"vo","lang":"Volapük","nativeName":"Volapük"},
     {"isocode":"wa","lang":"Walloon","nativeName":"Walon"},
     {"isocode":"cy","lang":"Welsh","nativeName":"Cymraeg"},
     {"isocode":"wo","lang":"Wolof","nativeName":"Wollof"},
     {"isocode":"fy","lang":"Western Frisian","nativeName":"Frysk"},
     {"isocode":"xh","lang":"Xhosa","nativeName":"isiXhosa"},
     {"isocode":"yi","lang":"Yiddish","nativeName":"ייִדיש"},
     {"isocode":"yo","lang":"Yoruba","nativeName":"Yorùbá"},
     {"isocode":"za","lang":"Zhuang, Chuang","nativeName":"Saɯ cueŋƅ, Saw cuengh"}
   ];

        var api = {};
        api.requestStatusLabel = {};
        api.requestStatusLabel[0] = 'IDLE';
        api.requestStatusLabel[1] = 'NO_DRIVERS';
        api.requestStatusLabel[2] = 'SUBMITTED';
        api.requestStatusLabel[3] = 'DRIVER_ANSWERED';
        api.requestStatusLabel[4] = 'PASSENGER_CANCELED';
        api.requestStatusLabel[5] = 'DRIVER_CANCELED';
        api.requestStatusLabel[6] = 'ON_RIDE';
        api.requestStatusLabel[7] = 'DRIVER_ABORTED';
        api.requestStatusLabel[8] = 'PASSENGER_ABORTED';
        api.requestStatusLabel[9] = 'ENDED';
        api.requestStatusLabel[10] = 'PAID';
        api.requestStatusLabel[11] = 'TERMINATED';
        api.requestStatusLabel[12] = 'FAILED';
        api.requestStatusLabel[13] = 'MISSING_FUNDS';
        api.requestStatusLabel[14] = 'CANCELLED_BY_SERVER';
        api.requestStatusLabel[15] = 'REFUNDED';
        api.requestStatusLabel[16] = 'ABORTED_UNPAID';

        api.requestStatusLabelFilter = {};
        api.requestStatusLabelFilter[0] = {label: 'TUTTI'};
        api.requestStatusLabelFilter[1] = {label: 'IDLE', value: 0};
        api.requestStatusLabelFilter[2] = {label: 'NO_DRIVERS', value: 1};
        api.requestStatusLabelFilter[3] = {label: 'SUBMITTED', value: 2};
        api.requestStatusLabelFilter[4] = {label: 'DRIVER_ANSWERED', value: 3};
        api.requestStatusLabelFilter[5] = {label: 'PASSENGER_CANCELED', value: 4};
        api.requestStatusLabelFilter[6] = {label: 'DRIVER_CANCELED', value: 5};
        api.requestStatusLabelFilter[7] = {label: 'ON_RIDE', value: 6};
        api.requestStatusLabelFilter[8] = {label: 'DRIVER_ABORTED', value: 7};
        api.requestStatusLabelFilter[9] = {label: 'PASSENGER_ABORTED', value: 8};
        api.requestStatusLabelFilter[10] = {label: 'ENDED', value: 9};
        api.requestStatusLabelFilter[11] = {label: 'PAID', value: 10};
        api.requestStatusLabelFilter[12] = {label: 'TERMINATED', value: 11};
        api.requestStatusLabelFilter[13] = {label: 'FAILED', value: 12};
        api.requestStatusLabelFilter[14] = {label: 'MISSING_FUNDS', value: 13};
        api.requestStatusLabelFilter[15] = {label: 'CANCELLED_BY_SERVER', value: 14};
        api.requestStatusLabelFilter[16] = {label: 'REFUNDED', value: 15};
        api.requestStatusLabelFilter[17] = {label: 'ABORTED_UNPAID', value: 16};

        api.requestStatus = {};
        api.requestStatus[0] = 'REQUEST_STATUS_IDLE';
        api.requestStatus[1] = 'REQUEST_STATUS_NO_DRIVERS';
        api.requestStatus[2] = 'REQUEST_STATUS_SUBMITTED';
        api.requestStatus[3] = 'REQUEST_STATUS_DRIVER_ANSWERED';
        api.requestStatus[4] = 'REQUEST_STATUS_PASSENGER_CANCELED';
        api.requestStatus[5] = 'REQUEST_STATUS_DRIVER_CANCELED';
        api.requestStatus[6] = 'REQUEST_STATUS_ON_RIDE';
        api.requestStatus[7] = 'REQUEST_STATUS_DRIVER_ABORTED';
        api.requestStatus[8] = 'REQUEST_STATUS_PASSENGER_ABORTED';
        api.requestStatus[9] = 'REQUEST_STATUS_ENDED';
        api.requestStatus[10] = 'REQUEST_STATUS_PAID';
        api.requestStatus[11] = 'REQUEST_STATUS_PASSENGER_TERMINATED';
        api.requestStatus[12] = 'REQUEST_PAYMENT_FAILED';
        api.requestStatus[13] = 'REQUEST_MISSING_FUNDS';
        api.requestStatus[14] = 'REQUEST_CANCELLED_BY_SERVER';
        api.requestStatus[15] = 'REQUEST_REFUNDED';
        api.requestStatus[16] = 'REQUEST_ABORTED_UNPAID';

      api.baseUrl = 'https://v2.sharethecity.net/zego/v1/';
      //  api.baseUrl = 'http://test.zegoapp.com:8080/zego/v1/';
      //api.baseUrl = 'http://192.168.1.122:8080/zego/v1/';
      //api.baseUrl = 'http://v2dev.shofer.com.au:8080/shofer2/v1/'
        var headers = {'zegotoken':'us_6c85ad9e-42f6-4c27-8627-13ecc042ad3f'};
        //var headers = {'zegotoken':'admintoken'};
        //var headers = {'zegotoken': ""};// $rootScope.userLogged.token};

        api.requestDriverStatus = {};

        api.requestDriverStatus[1] = 'CREATED';
        api.requestDriverStatus[2] = 'CANCELED';
        api.requestDriverStatus[3] = 'REJECTED';
        api.requestDriverStatus[4] = 'ACCEPTED';
        api.requestDriverStatus[5] = 'ACCEPTED_BY_OTHER';
        api.requestDriverStatus[6] = 'TOOLATE';



        api.saveLoginPersistent = function(user){
           // headers = {'zegotoken': user.token};
          $cookies.putObject("ZEGOuserLogged", user);
        }
        api.checkLoginPersistent = function(){
            if($cookies.getObject("ZEGOuserLogged")){

                $rootScope.userLogged =  $cookies.getObject("ZEGOuserLogged");
               // headers.zegotoken = $rootScope.userLogged.zegotoken;

                return;
            }
        }
        api.logout = function(){
            $rootScope.userLogged =null;
            $cookies.remove("ZEGOuserLogged");
        }

        api.callLogin = function(dati,c){
            api.user.single.login(dati).$promise.then(function(o){
                c(o);
            }).catch(function(o){
              c(false,o);
            })
        }

        api.adminlogin = function(dati,c){
          var r = $resource(api.baseUrl+'user/adminlogin',{},{
              post:{
                method : 'POST',
                isArray : false,
                headers:headers
              }
          });

          r.post({},dati).$promise.then(function(o){
            c(o)
          },function(){
             $mdDialog.show(
                    $mdDialog.alert()
                      .clickOutsideToClose(true)
                      .parent(angular.element(document.body))
                      .title('Errore')
                      .textContent('Non è stato possibile autenticarsi con le credenziali fornite.')
                      .ok('Go')
                  );
          })

        }
        api.login = function(dati, c){

            if($cookies.getObject("ZEGOuserLogged")){
                c($cookies.getObject("ZEGOuserLogged"));
                return;
            }


            api.user.single.validate({
              uid:dati.uid,
              pin: dati.pin}).$promise.then(function(login){
                if( login.code != undefined && login.code== 103){
                  c(false)
                }else{
                    $cookies.putObject("ZEGOuserLogged", login);
                    $rootScope.userLogged = login;
                  //  headers.zegotoken = login.zegotoken;
                    c(login);
                }
            }).catch(function(o){
              c(false);
            });





        }



        // <!-- OGGETTO RICERCA
        api.oggettoRicerca = function(filters){
          this.simple = [];
          this.range = [];
          this.like = [];
          this.sortfield = "id";
          this.direction = "desc";


          if( filters != undefined )
          this.buildFilters(filters);
          //this.headers = [];
        };
        api.oggettoRicerca.prototype.addField = function(f,v){
          if(v == null)
          return;
          this.simple.push(
            {
              field : f,
              filter : v
            }
          );
          this.checkHeader(f);
        }
        api.oggettoRicerca.prototype.addRange = function(f,from,to){

          console.log(from);
          console.log(typeof from);
          if(typeof to.getMonth == 'function' )
            to = Math.round(to.getTime() / 1000);

          if(typeof from.getMonth == 'function')
            from = Math.round(from.getTime() / 1000);

          this.range.push(
            {
              field : f,
              start : from,
              stop : to
            }
          )
          this.checkHeader(f);
        }
        api.oggettoRicerca.prototype.addLike = function(f,v){
          this.like.push(
            {
              field : f,
              term : v
            }
          )
          this.checkHeader(f);
        }
        api.oggettoRicerca.prototype.checkHeader = function(f){

        /*  if( this.headers.indexOf(f) == -1 ){
            this.headers.push(f);
          }*/
        }
        api.oggettoRicerca.prototype.buildFilters = function(filters){
          var self = this;
          angular.forEach(filters.simple,function(o,k){
            self.addField(k,o);
          });
          angular.forEach(filters.range,function(o,k){
            self.addRange(k,o.from,o.to);
          });
          angular.forEach(filters.like,function(o,k){
            self.addLike(k,o);
          });


          if( filters.direction != undefined )
            this.direction = filters.direction;
          if( filters.sortfield != undefined )
            this.sortfield = filters.sortfield;

        }
        api.oggettoRicerca.prototype.orderBy = function(f,d){
            this.sortfield = f;
            if( d!= undefined )
              this.direction = d;
        }
        // OGGETTO RICERCA -->

        api.advancedExport = $resource(api.baseUrl+':entity/advancedexport/:start/:stop',{},{
            post:{
              method : 'POST',
              isArray : false,
              headers:headers
            }
        });


          api.advancedExport = function(g,p,nomefile){
            var entity = g.entity;
            var start = g.start;
            var stop = g.stop;
            if(nomefile == undefined){
                nomefile = 'export-zego';
            }
            var h = angular.copy(headers);

                    //h['Content-Type']='text/csv';
                 return $http({
                    url : api.baseUrl+entity+'/advancedexport/'+start+'/'+stop,
                    method : 'POST',
                    data : p,
                    headers : h,
                responseType : 'arraybuffer'
            }).success(function(data, status, headers, config) {
                // TODO when WS success
                var file = new Blob([ data ], {
                    type : 'text/csv'
                });
                //trick to download store a file having its URL
                var fileURL = URL.createObjectURL(file);
                var a         = document.createElement('a');
                a.href        = fileURL;
                a.target      = '_blank';
                a.download    = nomefile+'.csv';
                document.body.appendChild(a);
                a.click();
            }).error(function(data, status, headers, config) {
                //TODO when WS error
            });

          }





        api.advancedSearch = $resource(api.baseUrl+':entity/advancedsearch/:start/:stop',{},{
            post:{
              method : 'POST',
              isArray : true,
              headers:headers
            }
        });
        api.advancedCount = $resource(api.baseUrl+':entity/advancedcount',{},{
            post:{
              method : 'POST',
              isArray : false,
              headers:headers
            }
        });


        api.payment = $resource(api.baseUrl+'payment',{},{
          main:{
            method : 'POST',
            isArray : false,
            headers:headers,
            url:api.baseUrl+'riderequest/payment/main'
          },
          other:{
            method : 'POST',
            isArray : false,
            headers:headers,
            url:api.baseUrl+'riderequest/payment/other'
          },
          ride:{
            method : 'GET',
            isArray : true,
            headers:headers,
            url:api.baseUrl+'payment/filter/rid/:rid/range/0/1000'
          }

        });


        /* VERSIONI */
        api.versione = new function(){
          var versioni =
          [
            {
              number : "1.0.1",
              date : "12 Dicembre 2016",
              message : "Aggiunte funzionalità zone e servizi"
            },
            {
              number : "1.0.2",
              date : "14 Dicembre 2016",
              message : "Feedback interfaccia, inizio Ride request per test",
              todo : [
                {
                  l : "Togliere blocco user modalità info",
                  t :"feedback",
                  d : true

                },
                {
                  l : "filtri per data, indirizzi, stato ride, area, driver e passeggero",
                  t :"feedback",
                  d : true

                },
                {
                  l : "manca per tutte le liste, il pulsante per esportare file TXT",
                  t :"feedback",
                  d : false
                }
              ]
            },
            {
              number : "1.0.3",
              date : "15 Dicembre 2016",
              message : "Feedback interfaccia, tab driver in ride request, tab ultime posizioni in user, inizio Heatmap, risolto bug update user",
            },
            {
              number : "1.0.4",
              date : "16 Dicembre 2016",
              message : "Filtri veloci su tabelle, check codice esistente in messaggi errore, export veloce",
            },
            {
              number : "1.0.5",
              date : "23 Dicembre 2016",
              message : "Filtri avanzati in user / riderequest. Heatmap.",
            },
            {
              number : "1.0.6",
              date : "29 Dicembre 2016",
              message : "Risoluzione parte test case 2.0",
            },
            {
              number : "1.0.7",
              date : "11 Gennaio 2017",
              message : "My zego, blacklist list e migliorie moduli precedenti",
            },
            {
              number : "1.0.8",
              date : "13 Gennaio 2017",
              message : "Impostazioni zone, inserimento aree, promo, assegnazione promo a user, login automatico da app",
            },
            {
              number : "1.0.9",
              date : "6 Febbraio 2017",
              message : "Aggiunti report",
            },
            {
              number : "1.1.0",
              date : "10 Febbraio 2017",
              message : "Risoluzione test list, nuovo utente, export",
            },
            {
              number : "1.1.1",
              date : "10 Febbraio 2017",
              message : "Feedback riderequest, lista ingaggi scheda utente, heatmap",
            },
            {
              number : "1.1.2",
              date : "13 Febbraio 2017",
              message : "Date filtri export",
            },
            {
              number : "1.1.3",
              date : "14 Febbraio 2017",
              message : "Date filtri export",
            },
            {
              number : "1.1.4",
              date : "15 Febbraio 2017",
              message : "Actions, notifications e edita in riderequest",
            },
            {
              number : "1.1.5",
              date : "17 Febbraio 2017",
              message : "Risoluzione bug",
            }
            ,
            {
              number : "1.1.6",
              date : "21 Febbraio 2017",
              message : "Risoluzione bug",
            }
            ,
            {
              number : "1.1.7",
              date : "23 Febbraio 2017",
              message : "Filtri drivers, risoluzione bug",
            }
            ,
            {
              number : "1.1.8",
              date : "27 Febbraio 2017",
              message : "Bug risolto backenduser, debt in user",
            }
            ,
            {
              number : "1.1.9",
              date : "28 Febbraio 2017",
              message : "Scheda riderequest",
            }
            ,
            {
              number : "1.1.10",
              date : "1 Marzo 2017",
              message : "Bug i miei viaggi e riderequest",
            }
            ,
            {
              number : "1.1.11",
              date : "3 Marzo 2017",
              message : "Dimensione font",
            },
            {
              number : "1.1.12",
              date : "6 Marzo 2017",
              message : "Risoluzione bug promo",
            },
            {
              number : "1.1.13",
              date : "7 Marzo 2017",
              message : "Risoluzione bug utente, riderequest, errori",
            },
            {
              number : "1.1.14",
              date : "8 Marzo 2017",
              message : "Risoluzione bug heatmap",
            },
            {
              number : "1.1.15",
              date : "8 Marzo 2017",
              message : "Risoluzione bug heatmap e creazione inapp dialogs",
            }
            ,
            {
              number : "1.1.16",
              date : "15 Marzo 2017",
              message : "Nuovi report",
            }
            ,
            {
              number : "1.1.17",
              date : "21 Marzo 2017",
              message : "Visualizzazione tabelle su mobile",
            },
            {
              number : "1.1.18",
              date : "21 Marzo 2017",
              message : "Validazione IBAN",
            }
            ,
            {
              number : "1.1.19",
              date : "30 Marzo 2017",
              message : "Fix bug validazione form utente",
            },
            {
              number : "1.1.20",
              date : "30 Marzo 2017",
              message : "Fix settimane viaggi driver",
            }
            ,
            {
              number : "1.1.21",
              date : "31 Marzo 2017",
              message : "Report operativi",
            }
            ,
            {
              number : "1.1.22",
              date : "31 Maggio 2017",
              message : "Driver Control Flag",
            }
            ,
            {
              number : "1.1.23",
              date : "13 Giugno 2017",
              message : "Driver Control Label",
            }
              ,
            {
              number : "1.1.24",
              date : "19 Giugno 2017",
              message : "Fix collegamento referente",
            } ,
            {
              number : "1.1.25",
              date : "13 Luglio 2017",
              message : "Colonne promo in utente",
            },
            {
              number : "1.1.26",
              date : "5 Ottobre 2017",
              message : "Fix servizi",
            },
            {
              number : "1.1.27",
              date : "6 Ottobre 2017",
              message : "Modifiche area driver",
            }




          ];

          this.corrente = function(){
            return versioni[versioni.length - 1];
          }
        }
        // Base Url
      //  api.baseUrl = 'app/data/';




        api.general = function(entity,dati){
          return $resource(api.baseUrl + entity + '/:id',{},{
              list:{
                method : 'GET',
                isArray : true,
                headers : headers,
                url : api.baseUrl + entity
              },
              get:{
                method : 'GET',
                headers:headers
              },
              pag:{
                method : 'GET',
                isArray : true,
                headers:headers,
                url:api.baseUrl+entity+'/range/:start/:stop/asc'
              },
              filter:{
                method : 'GET',
                isArray : true,
                headers:headers,
                url:api.baseUrl+entity+'/filter/:by/:value/range/:start/:stop'
              }

          });

        }


        // api.sample = $resource(api.baseUrl + 'sample/sample.json');
        /* ride request */

        api.heatmap =  $resource(api.baseUrl+'riderequest/heathmap/:start/:stop/:stato',{},{
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            }
        });


        api.riderequestdrivers =  $resource(api.baseUrl+'riderequestdrivers',{},{
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequestdrivers/filter/:field/:value/range/0/100000000'
            },
        });


        api.riderequest = $resource(api.baseUrl+'riderequest/:id',{},{
            updaterating : {
                method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'riderequest/updaterating/:id'
            },
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers
            },
            list:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest'
            },
            count:{
              method :'GET',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'riderequest/count'
            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest/range/:start/:stop/asc'
            },
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest/filter/:field/:value/range/0/1000'
            },
            getDrivers:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest/:id/drivers'
            },
            annulla:{
                method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'riderequest/:id/cancella'
            },

            useraction : {
                method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'useraction/filter/rid/:id/range/0/1000'
            },
            notifications : {
                method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'notifications/filter/rid/:id/range/0/1000'
            },
            feedback: {
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'feedback/filter/rid/:id/range/0/1000'
            }


        });

        /* API User */
        api.user ={};
        api.user.list =  $resource(api.baseUrl+'user',{},{
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            count:{
              method :'GET',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'user/count'
            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'user/range/:start/:stop/asc'
            },
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'user/filter/:field/:value/range/0/1000'
            },
            drivingnow:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'user/drivingnow'
            }

        });

        api.manufacturer = $resource(api.baseUrl+'manufacturer',{},{
          getAll:{
            method : 'GET',
            isArray : true,
            headers:headers,
            url : api.baseUrl+'manufacturer'
          },
        });

        api.driverdata = $resource(api.baseUrl+'driverdata/:id',{},{
          save :{
            method:'PUT',
            isArray : false,
            headers : headers,
            url : api.baseUrl+'driverdata/:id'
          },
          create : {
            method:'POST',
            isArray : false,
            headers : headers,
            url : api.baseUrl+'driverdata'
          }
          ,
          withstatus:{
            method : 'GET',
            isArray : true,
            headers:headers,
            url : api.baseUrl+'driverdata/withstatus/:status/range/:start/:stop/sorted/:sort'
          },
          countwithstatus:{
            method : 'GET',
            isArray : false,
            headers:headers,
            url : api.baseUrl+'driverdata/countwithstatus/:status/range/:start/:stop/sorted/:sort'
          }
        });

        api.cities = $resource(api.baseUrl+'city/pfx/:pfx',{},{
          query :{
            method:'GET',
            isArray : true,
            headers : headers
          }
        });
        api.nation = $resource(api.baseUrl+'nation',{},{
          query :{
            method:'GET',
            isArray : true,
            headers : headers
          }
        });

        api.user.single = $resource(api.baseUrl+'user/:id',{},{
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            webcreate :{
                method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl + 'user/webcreate'
            },
            unlock:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl + 'user/unlock'
            },
            kill:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl+'user/kill'
            },
            login:{
              method : 'POST',
              isArray : false,
              headers:{},
              url : api.baseUrl+'user/login'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers
            },
            delete : {
              method : 'DELETE',
              isArray : false,
              headers:headers
            },
            create:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl+'user'
            },

            ban:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl+'user/ban'
            },
            getBan:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url : api.baseUrl+'banhistory/filter/uid/:uid/range/0/100'
            },
            getCalls:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url : api.baseUrl+'nextipcall/user/:uid'
            }
            ,
            unban:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url : api.baseUrl+'user/unban'
            },
            getDriverData:{
              method:'GET',
              isArray : true,
              headers : headers,
              url : api.baseUrl+'driverdata/filter/uid/:id/range/0/100'
            },


            ultimePosizioni:{
              method:'GET',
              isArray : true,
              headers : headers,
              url : api.baseUrl+'location/filter/uid/:id/range/:start/:stop'
            }
            ,

            pagamentiContanti:{
              method:'GET',
              isArray : true,
              headers : headers,
              url : api.baseUrl+'cash/filter/did/:id/range/:start/:stop'
            }
            ,

            resend : {
              method:'POST',
              isArray : false,
              headers : headers,
              url : api.baseUrl+'user/resend'
            },
            validate : {
              method:'POST',
              isArray : false,
              headers : headers,
              url : api.baseUrl+'user/validate'
            }
            ,
            sbloccaGratuitamente : {
              method:'POST',
              isArray : false,
              headers : headers,
              url : api.baseUrl+'user/probono'
            }
            ,
            ritentaPagamento : {
              method:'POST',
              isArray : false,
              headers : headers,
              url : api.baseUrl+'user/paydebt'
            }

        });

        api.user.pin = $resource(api.baseUrl+'pin/filter/uid/:id/range/0/100',{},{
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            }
        });



        api.user.blacklistdevice = $resource(api.baseUrl+'blacklist',{},{
            post:{
              method : 'POST',

              headers:headers
            }
        });

        api.blacklist = $resource(api.baseUrl+'blacklist',{},{
          getAll : {
            method : 'GET',
            isArray : true,
            headers:headers
          },
            getByUser:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url: api.baseUrl+'blacklist/filter/uid/:uid/range/0/100'
            },
            getByDeviceid:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url: api.baseUrl+'blacklist/filter/deviceid/:id/range/0/100'
            },
            elimina : {
              method :'DELETE',
                headers:headers,
              url : api.baseUrl+'blacklist/:id'
            }
        });


        // CONF
        api.conf = $resource(api.baseUrl+'conf/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'conf'
            },
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'conf'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'conf/range/:start/:stop/asc'
            },
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'errormsg/filter/:field/:value/range/0/100'
            }

        });


        // CONF
        api.appversion = $resource(api.baseUrl+'appversion/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'appversion'
            },
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'appversion'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'appversion/range/:start/:stop/asc'
            }

        });





        // ZONE
        api.zone = $resource(api.baseUrl+'zone/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'zone'
            },
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'zone'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'zone/range/:start/:stop/asc'
            },
            addService: {
              method: 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'zoneservice'
            },
            removeService: {
              method: 'DELETE',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'zoneservice/:id'
            },
            getService: {
              method: 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'zoneservice/filter/zid/:id/range/0/1000'
            },

        });



        // SERVIZI
        api.servizi = $resource(api.baseUrl+'service/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'service'
            },
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            delete:{
              method : 'DELETE',
              isArray : false,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'service'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'service/range/:start/:stop/asc'
            }
        });

        // AREE
        api.area = $resource(api.baseUrl+'area/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'area'
            },
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            delete:{
              method : 'DELETE',
              isArray : false,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'area'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'area/range/:start/:stop/asc'
            }
        });

        api.userpromo = $resource(api.baseUrl+'userpromo/:id',null,{
             get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },

        });

        // promo
        api.promo = $resource(api.baseUrl+'promo/:id',null,{
          associaUser:{
            method:'POST',
            isArray:false,
            headers:headers,
            url : api.baseUrl + 'userpromo'
          },
          userRedeem:{
            method:'POST',
            isArray:true,
            headers:headers,
            url : api.baseUrl + 'promo/redeem'
          },
          disassociaUtente:{
            method:'DELETE',
            isArray:false,
            headers:headers,
            url : api.baseUrl + 'userpromo/:id'
          },
          utentiAssociati:{
            method:'GET',
            isArray:true,
            headers:headers,
            url : api.baseUrl + 'userpromo/filter/pid/:id/range/0/100000'
          },
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'promo'
            },
            get:{
              method : 'GET',
              isArray : false,
              headers:headers
            },
            delete:{
              method : 'DELETE',
              isArray : false,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'promo'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'promo/range/:start/:stop/asc'
            },
            byUser:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'userpromo/filter/uid/:uid/range/0/1000'

            },
            search :{
              method:'GET',
              isArray : true,
              headers : headers,
              url : api.baseUrl+'promo/pfx/:pfx'
            },
            batchredeem:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'promo/batchredeem'
            },

        });



        // dialog
        api.storicoviaggi = $resource(api.baseUrl+'dialog/:id',null,{
            comedriver:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest/webhistory/:uid/driver/:start/:stop'
            },
            comepasseggero:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'riderequest/webhistory/:uid/rider/:start/:stop'
            },

        });





        // dialog
        api.dialog = $resource(api.baseUrl+'dialog/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'dialog'
            },
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'dialog'
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers

            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'dialog/range/:start/:stop/asc'
            }

        });




        // Error msg
        api.errormsg = $resource(api.baseUrl+'errormsg/:id',null,{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'errormsg'
            },
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            post:{
              method : 'POST',
              isArray : false,
              headers:headers,
              url:api.baseUrl+'errormsg'
            },
            delete:{
              method : 'DELETE',
              isArray : false,
              headers:headers
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers
            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'errormsg/range/:start/:stop/asc'
            },
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'errormsg/filter/:field/:value/range/0/100'
            }

        });

        // FRODE

        api.fraud = $resource(api.baseUrl+'fraud',{},{
          reset : {
            method : 'POST',
            isArray : false,
            headers:headers,
            url:api.baseUrl+'user/fraudreset'
          }
        });
        // Lang
        api.langIsoAll = function(){ return langIso;}

        api.lang = $resource(api.baseUrl+'lang',{},{
            getAll:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            get:{
              method : 'GET',
              isArray : true,
              headers:headers
            },
            delete:{
              method : 'DELETE',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'lang/:id'
            },
            pag:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'user/range/:start/:stop/asc'
            },
            post : {
              method :'POST',
              headers : headers
            },
            put:{
              method : 'PUT',
              isArray : false,
              headers:headers,
              url : api.baseUrl+'lang/:id'

            },
            filter:{
              method : 'GET',
              isArray : true,
              headers:headers,
              url:api.baseUrl+'lang/filter/:field/:value/range/0/100'
            }
        });



        AWS.config.credentials.get(function(o){
          console.log(o);
        });



        api.aws = {

          upload : function(file,dir,checkT){
            $rootScope.$broadcast('startLoading');

            var d = new Date();
            var n = d.getTime();
            var risultato = $q.defer();

            var nome,tipo,body;

            if(checkT != undefined){
              nome = angular.copy(dir);
              tipo = checkT;
              body = file;
              dir = undefined;
            }else{
              nome=file.name;
              tipo = file.type;
              body = file;

            }

            if(dir != undefined){
              //var bucket = new AWS.S3({params: {Bucket: 'alstom-data/'+dir}});
              var bucket = new AWS.S3({params: {Bucket: 'zegoapp/driverdata'}});
            }
            else{
              //var bucket = new AWS.S3({params: {Bucket: 'alstom-data'}});
              var bucket = new AWS.S3({params: {Bucket: 'zegoapp/driverdata'}});
            }

            var params = {Key: n + nome, ContentType: tipo, Body: body};



            bucket.upload(params, function (err, data) {
              //results.innerHTML = err ? 'ERROR!' : 'UPLOADED.';
              if(err){
                alert("C'è stato un errore nel caricamente del file");
                risultato.resolve(err);
              }else{
                risultato.resolve(data);
              }
              $rootScope.$broadcast('endLoading');

            });
            return risultato.promise;

          }
        };

        return api;
    }

})();
