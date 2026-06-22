import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50,
  duration: '10s',
};

// Lista com as URLs de cada instância rodando em portas separadas
const urls = [
  'http://host.docker.internal:8080/transacoes',
  'http://host.docker.internal:8081/transacoes',
  'http://host.docker.internal:8082/transacoes'
];

export default function () {
  // Sorteia aleatoriamente qual instância vai receber esta requisição específica
  const urlAleatoria = urls[Math.floor(Math.random() * urls.length)];

  const payload = JSON.stringify({
    numeroCartao: '5555666677778888',
    senhaCartao: '9876',
    valor: 10.00,
    estabelecimentoId:1
  });

  const params = { headers: { 'Content-Type': 'application/json' } };

  // Dispara contra a instância sorteada
  const res = http.post(urlAleatoria, payload, params);

  check(res, {
    'status esperado (201 ou 422)': (r) => r.status === 201 || r.status === 422,
  });

  sleep(0.05);
}