# 저쩔서버 💁🏻
![제목 없음](https://user-images.githubusercontent.com/63996052/173191361-085d8888-5412-4eba-a103-fea9cc0bd781.png)

<div align="center"> 
냉장고 사진 촬영을 통한 가공식품 & 식재료 판별 및 레시피 추천
  
집 밥을 해먹고자 하는 자취생에게 요리 진입 장벽이 낮은 레시피 제공

<b>🧊 어쩔냉장고 🧊</b>
</div>


---

## 📘 Main Service

![image](https://user-images.githubusercontent.com/63996052/173191754-f5af2c49-4faa-4d8b-b7c1-c0263b23d7f7.png)

---

## 💻 Server Developers
<div align="center">
  
|                            주어진사랑                            |                          서동민                          |
| :----------------------------------------------------------: | :----------------------------------------------------------: | 
  
</div>

---

## 🔷 Git Branch Strategy

![image](https://user-images.githubusercontent.com/63996052/173192213-f70c886f-f463-4b3c-bdf7-74b2aea4b7d4.png)

백엔드 대부분의 코드를 혼자 작성한 관계로 `main - feature/{기능}` 형태로 브랜치 구성

각 브랜치에서 작업한 뒤, PR Approve 이후 main으로 merge

---

## Convention

**code**

google style guide

**commit**

ex) feat: image upload

---

## Project Foldering

```tsx
.gradle
.idea
build
gradle
.editorconfig
.gitignore
build.gradle
gradlew
gradlew.bat
settings.gradle
src
│─test
└─main/java/sogang.capstone.blahblahfridge
    │    
    │────config
    │      └─security
    │    
    │────controller
    │      └─request
    │      
    │────domain
    │      
    │────dto
    │      └─oauth
    │      
    │────exception
    │     
    │────persistence
    │
    │────service
    │
    └─BlahblahfridgeApplication
```

---

## 🧮 ERD

![image](https://user-images.githubusercontent.com/63996052/173191983-fc12930b-b192-4516-9577-de31c66790a9.png)

---

### 🛠️ API Doc

- [어쩔냉장고 API 명세서](https://www.notion.so/def5abfd70d24171abca1654fbfb00b7?v=f47af7a415a64399941004ef1e8783d2)


---


###### 2022 BlahBlahFridge Server
