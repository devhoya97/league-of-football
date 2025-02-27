# league-of-football
리그오브풋볼 - 풋살게임매칭 프로젝트

### 서비스 설명
리그오브레전드(LOL) 게임을 해본 적 있으신가요? 이 게임은 5:5 팀전임에도 불구하고 개별 플레이어의 실력을
정확하게 측정해서 랭크 점수를 기준으로 실력이 비슷한 플레이어간 게임을 매칭시켜 몰입도를 높입니다. 
또한 플레이어는 랭크를 올리기 위해 게임에 더 몰입하고 성취감을 느끼죠.   
리그오브풋볼 프로젝트는 이와 같은 시스템을 벤치마킹하여 여러 스포츠에서 플레이어에게 랭크를 부여하고, 이를 기준으로 게임을 매칭하기 위해 시작했습니다.
5:5 풋살 게임으로 프로젝트를 시작하지만 축구, 농구, 심지어 1:1 배드민턴 등 거의 모든 경쟁 스포츠로 확장이 가능합니다.
리그오브풋볼의 성장을 지켜봐주세요!

### 주요 기능
- 게임 매칭 
  - 플레이어는 매칭 유형, 지역, 날짜를 선택하여 대기열에 입장합니다.
    - 풋살장 추천을 위해 출발 위치를 선택할 수 있습니다. 선택하지 않을 시 플레이어의 주소가 출발 위치가 됩니다.
    - 시간대는 선택이 불가능 합니다. 매칭 속도가 충분히 빠를 경우, 시간대 선택할 수 있도록 개선 예정입니다.
    - 참여 가능한 대기열이 존재할 경우, 무조건 참여시킵니다. 참여 가능한 대기열이 여러 개일 경우, 가장 먼저 생성된 대기열에 참여시킵니다.
    - 참여 가능한 대기열이 존재하지 않을 경우, 해당 플레이어를 필두로 새로운 대기열을 생성합니다.
    - 랭크 매칭의 경우, 참여 가능한 대기열이 존재하더라도, 대기열의 평균 랭크 점수와 차이가 많이 나는 플레이어는 참여할 수 없습니다.
    - 특정 시간 이내에 매칭에 실패한 대기열은 폭파됩니다. (24시간 예정)
    - 대기열 폭파 시간에 가까워 질수록 랭크 점수 차이에 의한 거부 기준이 완화됩니다.
    - 매칭이 완료되기 전, 플레이어는 언제든 대기열에서 이탈할 수 있습니다.
  - 개인 랭크 매칭 
    - 플레이어가 선택한 지역, 날짜에 맞춰 비슷한 개인 랭크의 플레이어들을 매칭합니다. 
    - 게임의 승패에 따라 개인 랭크 점수가 반영됩니다.
  - 팀 랭크 매칭
    - 5명의 팀원을 모아서 팀 대기열에 입장해야 합니다.
    - 플레이어가 선택한 지역, 날짜에 맞춰 비슷한 팀 랭크의 플레이어들을 매칭합니다.
    - 게임의 승패에 따라 팀 랭크 점수가 반영됩니다.
  - 일반 매칭
    - 플레이어가 선택한 지역, 날짜에 맞춰 랭크에 상관없이 플레이어들을 매칭합니다.
    - 게임의 승패는 랭크 점수에 반영되지 않습니다.
- 랭크 관리
  - 랭크 점수는 오직 게임의 승패에만 관련있습니다. 즉 골을 넣은 횟수는 랭크 점수와 무관합니다.
    - 팀 평균 점수의 차이에 따라 가중치가 반영됩니다.
  - 게임의 승패는 과반수 이상의 플레이어가 투표한 승리팀이 승리한 것으로 간주합니다.
    - 게임이 성사된 날짜가 지나기 전까지 승패투표를 하지 않은 인원은 매너점수가 하락합니다.
    - 게임의 시작 시간 기준 2시간 이후부터, 승패 투표를 하지 않은 플레이어에게 1시간마다 승패투표 독려 알림이 전송됩니다.
  - 연승 또는 연패 중일 경우 랭크 점수의 등락폭이 커집니다. 
  - 오랜 기간 게임에 참여하지 않을 경우, 랭크 점수가 하락할 수 있습니다.
  - 1년마다 회원의 최고 랭크 점수를 기록합니다. 랭크 점수는 비공개가 불가능합니다.
  - 매 시즌 점수 등락폭이 큰 5판의 배치고사가 존재합니다.
    - 신규 유저의 경우 0점에서 배치고사를 시작합니다.
    - 기존 유저의 경우 지난 시즌의 마지막 점수를 기준으로 배치고사를 시작합니다.
  - 랭크는 브론즈, 실버, 골드, 플래티넘, 다이아, 챌린저로 구분됩니다.
  - 랭크는 개인 랭크 점수와 팀 랭크 점수로 구분됩니다.
- 풋살장 예약
  - 10명의 플레이어 중 랜덤으로 한 명을 풋살장 예약자로 선정합니다.
    - 예약자는 풋살장 구장비용 1/N에서 제외됩니다. 
  - 지도 API를 활용해 모든 플레이어의 출발지점 10개의 중간 지점을 찾아내 풋살장을 추천합니다.
  - 예약자가 풋살장을 선택하여 투표에 올립니다.
    - 만장일치 찬성이 나오지 않으면 게임은 취소됩니다.
    - 특정 시간 내에 투표를 완료하지 못한 플레이어가 존재하면 게임은 취소되고 투표하지 않은 플레이어는 제제를 받습니다. (4시간 예정)
    - 잦은 거절은 게임의 원활한 진행을 방해하므로 거절 횟수가 카운팅됩니다. 
  - 풋살장 예약에 성공하면 예약자 외 9명의 플레이어에게 알림이 전송됩니다.
    - 예약자는 예약에 성공하면 영수증 사진 또는 캡쳐본을 업로드해야 합니다. 
    - 예약자는 예약한 풋살장 정보와 구장비의 1/9 금액이 얼마인지 알림에 기입해야 합니다.
    - 예약자 외 9명의 플레이어가 1/N 금액을 리그오브풋볼 측에 결제합니다.
    - 1/N 완료 시 해당 금액을 예약자에게 전달하고, 모든 플레이어에게 결제가 완료되어 게임이 승인됐다는 알림을 전송합니다.
    - 24시간 내에 1/N에 실패하면, 1/N에 참여하지 않은 플레이어들이 구장 비용을 모두 부담하고 게임이 취소됩니다.
    - 1/N에 참여하지 않은 플레이어는 대화방에서 강제퇴장 처리됩니다.
      - 게임이 취소돼도 대화방은 유지되므로, 플레이어들은 자율적으로 지인을 모아 구장을 이용할 수 있습니다.
- 대화방
  - 매칭이 성사되는 동시에 대화방이 열립니다.
  - 대화방에서 서로의 프로필을 확인할 수 있습니다.
  - 프로필을 통해 개인 대화방을 열 수 있습니다.
  - 채팅을 아직 확인하지 않은 인원수를 볼 수 있습니다.
- 파토 방지
  - 풋살장 예약 이후, 파토 시 구장 비용을 모두 부담해야 합니다.
  - 모든 플레이어는 첫 게임을 돌리기 전, 계약금 10만원을 리그오브풋볼 측에 결제해야 하며, 계약금을 반환받은 경우 매칭에 참여할 수 없습니다.
  - 파토 때문에 계약금이 차감된 경우, 다시 10만원을 채워야 매칭에 참여가 가능합니다.
  - 9명의 플레이어로부터 만장일치 파토 신고를 받으면, 해당 플레이어는 파토 플레이어로 선정됩니다.
  - 파토 사유를 나머지 플레이어가 이해하지 못하는 경우, 비매너 투표를 받게 되고, 투표가 일정 수를 넘으면 영구제제를 받습니다.
- 차단
  - 갈등이 있었던 플레이어를 차단하면 다음부터 해당 플레이어와 매칭되지 않습니다.
- 친구 기능
- 플레이어 검색 기능
  - 플레이어의 최근 전적과 시즌별 랭크 점수를 확인할 수 있습니다.
- 신고

### 기술 스택
Java, SpringBoot, JPA, Spring Data JPA, JUnit5, MySQL, H2 
