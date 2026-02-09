import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    contacts: {
      executor: 'shared-iterations',
      vus: 100,
      iterations: 100,
      maxDuration: '30s',
    },
  },
};

export default function () {
  let res = http.post('http://localhost:8080/concerts/1/decrease-seats?quantity=1');
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}
