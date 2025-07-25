<p align="center">
  <img width="469" height="281" alt="image" src="https://github.com/user-attachments/assets/d2669ca9-1a02-465d-883c-edff575e0948" />
</p>

# AceNetwork: Minecraft Web3 Economy Server

---

## 📖 Visão Geral (pt\_BR)

AceNetwork é um servidor de Minecraft customizado com economia Web3 integrada, que combina transações on‑chain, NFTs, play‑to‑earn e minigames. Iniciado em novembro de 2021, passou por várias fases de Alpha, Beta e eventos de pré-venda, oferecendo duas modalidades interativas: **Ace Survival** (Minecraft) e **Ace Miner** (clicker game).

## 📖 Overview (en\_US)

AceNetwork is a custom Minecraft server with built‑in Web3 economy, blending on‑chain transactions, NFTs, play‑to‑earn mechanics, and minigames. Launched in November 2021, the project evolved through Alpha, Beta, and presale phases, featuring two interactive modes: **Ace Survival** (Minecraft) and **Ace Miner** (clicker game).

---

## 🚀 Funcionalidades Principais & Key Features

| Funcionalidade / Feature                                                               | Descrição (pt\_BR)                                                                                                                       | Description (en\_US)                                                                                                           |
| -------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| Economia Web3                                                                          | Integração com carteiras, NFTs e token \$ACE/\$BTA on‑chain dentro do jogo.                                                              | On‑chain wallet integration, NFT trading, and \$ACE/\$BTA token economy.                                                       |
| Minigame Ace Survival                                                                  | Construção e gestão de cidades, PvP de raids, mundos Newbie e Raid, rastreadores e sistema de raridades.                                 | City‑building and management minigame with raid PvP, Newbie & Raid worlds, trackers, and item tiers.                           |
| Minigame Ace Miner                                                                     | Clicker game paralelo que interage com o servidor Minecraft, farm de pickaxes e tokens em duplas.                                        | Parallel clicker game interacting with Minecraft server; farm pickaxes and tokens across modes.                                |
| Wallet Auth & Permissões                                                               | Autenticação customizada (UUID v3/v4), sistema de permissões granular via BungeeCord Proxy e Limbo.                                      | Custom authentication (UUID v3/v4), granular permissions framework via BungeeCord Proxy and Limbo server.                        |
| GUI Customizado & Internacionalização                                                  | Interfaces in‑game para mercado, leilões e clans; suporte multilíngue via `.properties`.                                                 | In‑game GUI for marketplace, auctions, clans; multilanguage support with `.properties` files.                                  |
| Land Claims & Territórios                                                              | Sistema de reivindicação de terrenos, permitindo jogadores claimar, proteger e gerenciar permissões de suas lands.                       | Land claim system enabling players to claim, protect, and manage permissions for their lands.                                  |                                                                                         |
| Jackpot & Recompensas                                                                  | Implementação de jackpot on‑chain: jogadores contribuem tokens em eventos, e um vencedor é sorteado para receber a recompensa acumulada. | On‑chain jackpot implementation: players contribute tokens in events, and a winner is drawn to receive the accumulated reward. |
| Pipelines & Infraestrutura                                                             | Deploy automatizado CI/CD, backups, monitoramento e escalonamento AWS/hosts dedicados.                                                   | Automated CI/CD deployment, backup routines, monitoring and AWS/dedicated host scaling.                                        |

---

## 🏗️ Arquitetura / Architecture

```txt
        Minecraft Client  ↔  BungeeCord Proxy  ↔  Game Servers (Paper)
                                    |
                                    v
                        +----------------------+
                        |   API Gateway (Node) |
                        +-----+-----+-----+----+
                              |     |     |
              +---------------+     |     +---------------+
              v                     v                     v
        Tx Service           Wallet Auth           MongoDB / Moralis
      (NodeJS, Shell)       (NodeJS)             (Dados de jogo e NFTs)
```

---

## 📅 Roadmap & Histórico / Timeline & History

| Data            | Evento                                                                                                                               |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| Nov 30, 2021    | Lançamento do Whitepaper Beta (EN/PT) e abertura de Beta fechado para originais.                                                     |
| Dec 31, 2021    | Anúncio de duas modalidades (Survival e Miner), com presale de pickaxes (\$ACE).                                                     |
| Jan 25, 2022    | Conclusão do desenvolvimento principal; website, clicker game e servidor Minecraft prontos para testes.                              |
| Feb 19–28, 2022 | Open Beta gratuito; até 330 \$ACE distribuídos por dias de shard collection; eventos de parkour, roleta e battle royale com prêmios. |
| Mar 06, 2022    | Lançamento do novo mundo paralelo (nova seed) e ajuste de dupla coleta de shards.                                                    |
| Mar 17, 2022    | Implantação do Marketplace de Clãs e sistema de referência beta.                                                                     |
| Mar 30, 2022    | Anúncio do token BETA-ACE (\$BTA): supply 25k, price US\$0.04, tokenomics 10/85/5.                                                   |
| Apr 07, 2022    | Reset de mundo e migração para \$BTA como moeda principal na temporada 0.                                                            |
| May 28, 2022    | Lançamento da coleção ACE LANDS (132 NFTs representando territórios no Spawn) no OpenSea.                                            |
| Aug 20, 2022    | Projeto descontinuado devido à nova EULA da Microsoft, que não permite mais servidores com economia em criptomoeda.                  |
