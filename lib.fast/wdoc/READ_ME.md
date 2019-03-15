lib.fast 帮助项目快速开发  
versionCode:4  
versionName:V0.0.4  
data:2018.12.19 15:37  

1.BaseActivity封装常用的一些方法：输入法弹出和隐藏、Eventbus事件、Loading、Toast、标题栏等

2.BaseMVPActivity封装MVP在其中，View层继承BaseMVPActivity即可，Presenter继承BasePresenter即可、Model层继承IModel即可

3.recyclerview的下拉刷新使用的开源项目:https://github.com/anzaizai/EasyRefreshLayout

4.swiplayout 使用的开源项目:https://github.com/anzaizai/EasySwipeMenuLayout

5.依赖注入框架使用的是ButterKnife

6.recyclerview.adapter 使用的是开源项目BaseRecyclerViewAdapterHelper 简洁Adapter的代码量. 项目地址:https://github.com/CymChad/BaseRecyclerViewAdapterHelper  
(注意:要想使用该项目,需要在项目根目录下的build.gradle文件中的allprojects/repositories下加入:maven { url "https://jitpack.io" })

7.popwindow 使用的开源项目EasyPopup,简洁popwindow的代码量 本地ReadMe:Easy_Pop_README.md 项目地址:https://github.com/zyyoona7/EasyPopup

8.网络框架 Retorfit2+RxJava2+Okhttp3

9.数据库:lib.sqlcipherpal,加密sqlite数据库

10.选择器: 仿IOS选择器CharacterPickerView,做多只支持三级联动 本地ReadMe:CharacterPickerView_README.md 项目地址:https://github.com/ImKarl/CharacterPickerView