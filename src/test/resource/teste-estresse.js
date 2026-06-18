import http from 'k6/http';
import { check, sleep } from 'k6';

// Configuração do cenário de estresse
export const options = {
  vus: 50,          // 50 usuários virtuais disparando ao mesmo tempo
  duration: '10s',  // O teste vai rodar por 10 segundos seguidos
};

export default function () {
  const url = 'http://localhost:8080/transacoes';

  const payload = JSON.stringify({
    numeroCartao: '6549873025634501',
    senhaCartao: '1111',
    valor: 10.00
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // Dispara o POST contra a API Spring Boot
  const res = http.post(url, payload, params);

  // Valida se a resposta foi 201 (Sucesso) ou 422 (Saldo Insuficiente controlado pelo motor)
  // Se der qualquer HTTP 500, o K6 vai acusar falha
  check(res, {
    'status esperado (201 ou 422)': (r) => r.status === 201 || r.status === 422,
  });

  // Um pequeno delay de 50ms entre os tiros de cada usuário para não travar a máquina local
  sleep(0.05);
}