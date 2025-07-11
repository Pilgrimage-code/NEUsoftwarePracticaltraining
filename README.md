

# 测盟汇管理系统 (CEMH)

测盟汇管理系统（Campus Educational Meeting Hub，CEMH）是一个全面的企业级多租户应用程序，旨在为教育机构提供会议管理、课程管理、用户管理和部门管理等功能。系统采用前后端分离架构，支持多租户隔离，为不同的机构提供独立的环境。

<!-- PROJECT SHIELDS -->

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />

<p align="center">
  <a href="https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">"测盟汇"README</h3>
  <p align="center">
    快速开始项目！
    <br />
    <a href="https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining"><strong>探索本项目的文档 »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining">查看Demo</a>
    ·
    <a href="https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues">报告Bug</a>
    ·
    <a href="https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues">提出新特性</a>
  </p>

</p>


 本篇README.md面向开发者
 
## 目录

- [上手指南](#上手指南)
  - [开发前的配置要求](#开发前的配置要求)
  - [安装步骤](#安装步骤)
- [文件目录说明](#文件目录说明)
- [部署](#部署)
- [使用到的框架](#使用到的框架)
- [项目细节](#项目细节)
- [贡献者](#贡献者)
  - [如何参与该开源项目](#如何参与该开源项目)
- [版本控制](#版本控制)
- [鸣谢](#鸣谢)

### 上手指南


###### 开发前的配置要求

1. xxxxx x.x.x
2. xxxxx x.x.x

###### **安装步骤**

1. Get a free API Key at [https://example.com](https://example.com)
2. Clone the repo

```sh
git clone https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining.git
```

### 文件目录说明

```
后端文件目录
cemh-backend/
├── src/main/
│   ├── java/com/cemh/
│   │   ├── common/      # 通用类和工具
│   │   ├── config/      # 配置类
│   │   ├── controller/  # 控制器
│   │   ├── dto/         # 数据传输对象
│   │   ├── entity/      # 实体类
│   │   ├── handler/     # 处理器
│   │   ├── mapper/      # MyBatis映射器
│   │   ├── service/     # 服务接口
│   │   │   └── impl/    # 服务实现
│   │   ├── utils/       # 工具类
│   │   └── vo/          # 视图对象
│   └── resources/
│       ├── mapper/      # MyBatis XML映射文件
│       ├── application.properties # 应用配置
│       └── application.yml       # YAML配置
└── pom.xml              # Maven配置文件
```
```
前端文件目录
cemh-frontend/
├── dist/                    # 构建输出目录
│   ├── assets/
│   │   ├── css/
│   │   └── js/
├── node_modules/            # 依赖包目录
├── public/                  # 静态资源目录
├── src/                     # 源代码目录
│   ├── api/                 # API接口定义
│   ├── assets/              # 静态资源
│   │   └── css/
│   ├── components/          # 公共组件
│   ├── router/              # 路由配置
│   ├── store/               # 状态管理
│   ├── styles/              # 样式文件
│   ├── utils/               # 工具函数
│   └── views/               # 页面组件
│       ├── Chat/            # 聊天功能
│       ├── auth/            # 认证相关
│       ├── course/          # 课程管理
│       ├── dashboard/       # 仪表板
│       ├── error/           # 错误页面
│       ├── layout/          # 布局组件
│       ├── meeting/         # 会议管理
│       ├── news/            # 新闻管理
│       ├── system/          # 系统管理
│       └── user/            # 用户管理
```

### 部署

#### 前端部署

```bash
# 进入前端目录
cd cemh-frontend

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境构建
npm run build
```

#### 后端部署

```bash
# 进入后端目录
cd cemh-backend

# 编译打包
mvn clean package

# 运行应用
mvn spring-boot:run
```

### 使用到的框架

- [xxxxxxx](https://getbootstrap.com)
- [xxxxxxx](https://jquery.com)
- [xxxxxxx](https://laravel.com)

### 项目细节 

请阅读[ARCHITECTURE.md](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/ARCHITECTURE.md) 查阅为该项目的架构。

### 贡献者

请阅读**CONTRIBUTING.md** 查阅为该项目做出贡献的开发者。
测盟会开发小组全部成员
 *您也可以在贡献者名单中参看所有参与该项目的开发者。*
 
##### 如何参与该开源项目

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。


1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



### 版本控制

该项目使用Git进行版本管理。您可以在repository参看当前可用版本。



### 版权说明

该项目签署了MIT 授权许可，详情请参阅 [LICENSE.txt](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/LICENSE.txt)

### 鸣谢


- [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
- [Img Shields](https://shields.io)
- [Choose an Open Source License](https://choosealicense.com)
- [GitHub Pages](https://pages.github.com)
- [Animate.css](https://daneden.github.io/animate.css)
- [xxxxxxxxxxxxxx](https://connoratherton.com/loaders)

<!-- links -->
[your-project-path]:Pilgrimage-code/NEUsoftwarePracticaltraining
[contributors-shield]: https://img.shields.io/github/contributors/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=flat-square
[contributors-url]: https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=flat-square
[forks-url]: https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/network/members
[stars-shield]: https://img.shields.io/github/stars/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=flat-square
[stars-url]: https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/stargazers
[issues-shield]: https://img.shields.io/github/issues/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=flat-square
[issues-url]: https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues
[license-shield]: https://img.shields.io/github/license/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=flat-square
[license-url]: https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/LICENSE.txt





