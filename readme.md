# Seldon Fleet - Monitoramento de Dispositivos Android

Aplicativo Android desenvolvido em Kotlin e Jetpack Compose para monitoramento de frotas de dispositivos a partir de dados de telemetria locais (frota.json), com regras de negocio isoladas, testes unitarios na JVM e persistencia local de favoritos.

---

## Como Executar
1. Clone o repositorio ou abra o projeto no Android Studio.
2. Certifique-se de que o arquivo frota.json esta posicionado na pasta app/src/assets/frota.json.
3. Ative o Modo Aviao no emulador ou dispositivo fisico para verificar que o app funciona 100% offline (requisito REQ-01).
4. Clique em Run para compilar e instalar.
5. Para rodar os testes unitarios da regra de negocio rapidamente na JVM (sem emulador), execute no terminal:
   ```bash
   ./gradlew test
   ```

---

## Decisoes de Arquitetura e Trade-offs
- Clean Architecture: O projeto foi dividido em camadas estritas (domain, data e ui). 
- Dominio Isolado (domain/): Contem as regras de classificacao em Kotlin puro, sem nenhuma dependencia do Android (zero import android.*), permitindo que os testes executem em poucos milissegundos na JVM.
- Inversao de Dependencia: O dominio declara a interface do repositorio (DeviceRepository) e a camada de dados a implementa, garantindo baixo acoplamento.
- Persistencia de Favoritos: Utiliza o Jetpack DataStore (Preferences) para garantir que o estado de favoritos sobreviva ao encerramento completo do processo, mantendo o arquivo frota.json como fonte da verdade imutavel para a telemetria.

---

## O que eu faria em seguida (Proximos Passos)
- Implementar paginacao ou busca textual avancada por modelo/ID caso o volume da frota cresca para milhares de dispositivos.
- Adicionar graficos analiticos de distribuicao de dreno por tier de SoC utilizando bibliotecas leves de Canvas no Jetpack Compose.

---

## Declaracao de Uso de IA
O uso de assistentes de Inteligencia Artificial foi empregado como suporte no desenvolvimento para estruturacao de diretrizes da Clean Architecture, validacao sintatica do Kotlinx Serialization e estruturacao inicial dos casos de teste de fronteira. Todo o codigo foi rigorosamente auditado, integrado e testado manualmente.