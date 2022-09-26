급여 계산기
===
# 요구사항
* 근무 `시작 시간`과 근무 `종료 시간`으로 `급여를 계산`한다.
* `1일 정규 근무 시간`은 `8시간` 이다.
* `8시간 근무` 시, `1시간의 휴게 시간`이 주어진다.
  * `최대 휴게 시간`은 `1시간`이다.
* `1일 정규 근무 시간을 초과`하는 경우, `연장 근무 시간`으로 산정한다.
* `22:00 ~ 익일 06:00 사이`의 근무는 `야간 근무 시간`으로 산정한다.
* `정규 근무 시간`의 급여는 `시간당 만원`으로 산정한다.
* `연장 근무 시간`의 급여는 `분당 100원`으로 산정한다.
* `야간 근무 시간`의 급여는 `분당 150원`으로 산정한다.
* `1원 단위`는 `절삭`한다.

# 구현 기능 목록
## 근태를 표현하는 객체 설계
* [x] 근태 관리 및 근무 시간, 급여 산출에 필요한 속성 정의
* [x] 연장 근로를 표현하는 객체 설계
* [ ] 야간 근로를 표현하는 객체 설계

## 급여 명세서 출력 부 구현
* [x] 일자가 포함된 출/퇴근 시간을 산출한다. (형식:YYYY-MM-DD hh:mm)
* [x] 출/퇴근 시간으로부터 전체 실 근무 시간을 산출한다. (형식 : hh:mm) 
* [x] 연장 근무 산출
  * [x] 1일 정규 근무 시간을 초과하는 연장 근무 시간을 산출한다. (형식 : hh:mm)
  * [ ] 연장 근무 시간에 대한 연장 근로 수당을 산출한다. (단위 : 원, 1원 단위 절삭)
* [ ] 야간 근무 산출
  * [ ] 22:00 ~ 익일 06:00 사이 야간 근무 시간을 산출한다. (형식 : hh:mm)
  * [ ] 야간 근무 시간에 대한 야간 근로 수당을 산출한다. (단위 : 원, 1원 단위 절삭)
* [ ] 출/퇴근 시간으로부터 기본급, 연장/야간 근로 수당이 합산된 전체 급여 총액을 산출한다. (단위 : 원, 1원 단위 절삭) 

## 급여 명세서 출력 예제
* 급여 명세서
  * 출/퇴근 시간 
  * 급여 총액
  * 연장 근무 시간
  * 연장 근무 수당
  * 야간 근무 시간
  * 야간 근무 수당
```
출근 : 2022-09-23 23:00
퇴근 : 2022-09-24 11:00
----------
급여 총액 : 201,000원
총 근로 시간 : 12시간
연장 근무 시간 : 3시간 0분, 08:00 ~ 11:00
연장 근로 수당: 18,000원
야간 근무 시간 : 7시간, 23:00 ~ 06:00
야간 근로 수당 : 54,000원
```
---

