# 가계부 웹 어플리케이션 만들기
### 프로젝트 소개

---

본 프로젝트는 Spring boot와 Thymeleaf 활용법을 배우면서 만든 가계부 웹 어플리케이션 입니다.

또한 실제 가계부를 사용하면서 있었으면 좋겠다고 생각한 기능들을 추가하여 개발해 보았습니다.

본 어플리케이션의 핵심 기능은 **엑셀 업로드**로 쉽게 가계부를 작성할 수 있다는것과 SSE 기술을 활용하여 **실시간 지출 현황을 확인,** **지출한 지역을 확인** 할 수 있는 기능을 핵심 기능이라고 생각합니다.

### 개발환경 및 실행방법

---

#### *개발환경

![setting1](https://github.com/JinSungYoon/accountBook/blob/main/README/setting1.png)

**JAVA version** : 1.8

**JAVA Spring-Boot** : 2.6.2

**사용한 라이브러리 :**

```Java
// JDBC, JPA 설정
implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
// Spring Boot, Web, Security
implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.1'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
// Thymeleaf(Frontend)
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
// Excel upload를 위한 설정
implementation group: 'org.apache.poi', name: 'poi', version: '3.17'
implementation group: 'org.apache.poi', name: 'poi-ooxml', version: '3.17'
implementation group: 'commons-io', name: 'commons-io', version: '2.6'
implementation group: 'org.apache.commons', name: 'commons-lang3', version: '3.0'
// jquery,Boot Strap,font-awesome을 내장하기 위한 설정
implementation group: 'org.webjars', name: 'jquery', version: '3.4.1'
implementation group: 'org.webjars', name: 'popper.js', version: '1.16.0'
implementation group: 'org.webjars', name: 'bootstrap', version: '4.6.0'
implementation group: 'org.webjars', name: 'font-awesome', version: '4.7.0'
// gradle에 선언된 버전을 사용하도록(href에 버전 정보를 적지 않아도 됨)
implementation group: 'org.webjars', name: 'webjars-locator-core', version: '0.35'
// jquery-ui & monthpicker import
implementation group: 'org.webjars', name: 'jquery-ui', version: '1.12.1'
implementation group: 'org.webjars.npm', name: 'jquery-ui-monthpicker', version: '1.0.1'
implementation group: 'org.webjars', name: 'bootstrap-datetimepicker', version: '2.3.5'
// Json 설정, log4jdbc 설정, lombok
implementation group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'
implementation group: 'org.bgee.log4jdbc-log4j2', name: 'log4jdbc-log4j2-jdbc4.1', version: '1.16'
compileOnly 'org.projectlombok:lombok'
// Mysql 연결, spring boot configuration,lombok, security-test,spring-test
runtimeOnly 'mysql:mysql-connector-java'
annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
annotationProcessor 'org.projectlombok:lombok'
developmentOnly 'org.springframework.boot:spring-boot-devtools'
testImplementation "org.mybatis.spring.boot:mybatis-spring-boot-starter-test:2.2.1" 
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'org.springframework.security:spring-security-test'
```

#### *실행방법

AccountBookApplication에서 우클릭 후 Run As에서 Spring Boot App or Java Application을 선택하여 실행한다.

![setting2](https://github.com/JinSungYoon/accountBook/blob/main/README/setting2.png)

### 화면설명

---

- 로그인
  ![login](https://github.com/JinSungYoon/accountBook/blob/main/README/login.png)

- 대쉬보드(메인)
  ![dashboard](https://github.com/JinSungYoon/accountBook/blob/main/README/dashboard.png)

  월별 지출에 대하여, #결제지역, #일별 누적 지출내역, #분류별 지출, #결제빈도 카테고리, #일자별 지출내역을 확인할 수 있는 화면이다.

- 가계부작성
  ![transaction](https://github.com/JinSungYoon/accountBook/blob/main/README/transaction.png)

  실제 가계부를 작성하는 화면으로 직접 거래내역을 입력할 수 있으며, 엑셀 업로드를 통해 거래내역을 입력할 수 있다.

- 장소매핑
  ![locationMapping](https://github.com/JinSungYoon/accountBook/blob/main/README/locationMapping.png)

  방문한 장소의 분류와 위치를 매핑시키는 화면이다.
  
  

### 기능설명

---

* 대쉬보드(메인)
  * 결제지역 Display
    * 적용 기술 : 카카오 마커 클러스터리
    * 기술 설명 : 지역정보를 기반으로 군집화 하는 기설입니다.
  * 실시간 데이터 전송
    * 적용 기술 : Server Sent Event(SSE)
    * 기술 설명 : 서보에서 실시간으로 Client단으로 Streaming 하는 기술입니다.
* 가계부작성
  * 엑셀 업로드
* 장소매핑
  * 지역매핑
    * 적용기술 : 카카오지도
    * 기술설명 : 가맹점명을 넘겨 위치 정보를 지도에 보여주는 기능입니다.

### Contributing

---



### License

---







