<details><summary>AWS 설정 화면 캡쳐</summary>

### EC2
#### Instance

<img src="images/01. EC2-Instance.png"/>

#### Security

<img src="images/02. Instance-Security.png"/>

#### Elastic IP
<img src="images/03. Elastic IP address.png"/>

### RDS
#### Database

<img src="images/04. RDS.png"/>

#### Configuration

<img src="images/05. RDS-Configuration.png"/>

#### Security

<img src="images/06. RDS-Security.png"/>

#### Parameter Group

<img src="images/07. RDS-ParameterGroup01.png"/>
<img src="images/08. RDS-ParameterGroup02.png"/>

### S3
#### Buckets

<img src="images/09. S3.png"/>

#### Permissions

<img src="images/10. S3-Permissions.png"/>

### AIM
#### User

<img src="images/11. AIM 유처추가.png"/>
</details>

<details><summary>대용량 데이터 처리</summary>

|방법|시도 횟수|소요 시간|
|---|---|---|
|기본 `JPA`방식|100|`3min 51sec`|
|`password` 컬럼을 조회에서 제외|100|`3min 2sec`|
|`nickname`컬럼에 인덱싱|100|`10ssec 67ms`|
|`nickname`컬럼에 인덱싱 & `password` 컬럼을 조회에서 제외|100|`11sec 181ms(?)`|

#### 결론
- 크기가 크거나 특정 상황 때 필요한 컬럼은 제외하면 조회 속도가 향상된다.
- 자주 사용하는 컬럼에 인덱스를 걸면 조회 속도가 향상된다.
- **왜인지 이 둘을 혼용하면 조회 속도가 더 떨어진다.**

</details>