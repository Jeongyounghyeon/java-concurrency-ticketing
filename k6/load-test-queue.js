import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    contacts: {
      executor: 'per-vu-iterations',
      vus: 100,
      iterations: 1, // 각 유저당 1번 실행 (대기열 등록 -> 대기 -> 주문)
      maxDuration: '1m',
    },
  },
};

export default function () {
  const userId = `user-${__VU}`; // 유저 ID 생성 (user-1, user-2, ...)

  // 1. 대기열 등록
  let res = http.post(`http://localhost:8080/queue/tokens?userId=${userId}`);
  
  if (res.status !== 200) {
      console.error(`Registration failed for ${userId}: ${res.status} ${res.body}`);
  }
  
  check(res, { 'Registered': (r) => r.status === 200 });

  // 2. 대기 상태 확인 (Polling)
  while (true) {
    res = http.get(`http://localhost:8080/queue/tokens/status?userId=${userId}`);
    if (res.body === 'ALLOWED') {
      break; // 입장 허용됨
    }
    sleep(1); // 1초 대기 후 재시도
  }

  // 3. 재고 차감 요청
  res = http.post(`http://localhost:8080/concerts/1/decrease-seats?id=1&quantity=1&userId=${userId}`);
  
  if (res.status !== 200) {
      console.error(`Order failed for ${userId}: ${res.status} ${res.body}`);
  }

  check(res, {
    'Order Success': (r) => r.status === 200,
  });
}
