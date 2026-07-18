# 📚 StudyMate

Assistente de estudos com Inteligência Artificial, desenvolvido como projeto acadêmico full-stack em Java.

O StudyMate permite que estudantes organizem suas matérias, gerem exercícios dissertativos automaticamente com IA, recebam correção e feedback personalizado, resumam conteúdos e acompanhem sua evolução através de um dashboard de estatísticas.

---

## Funcionalidades

- **Autenticação segura** — cadastro e login com senhas criptografadas via BCrypt
- **Gestão de matérias (CRUD)** — criação, edição, listagem e remoção de matérias de estudo
- **Geração de exercícios com IA** — a IA cria exercícios dissertativos personalizados sobre qualquer matéria cadastrada
- **Correção automática** — respostas dos alunos são avaliadas pela IA, com feedback construtivo e dica de melhoria
- **Resumo de conteúdos** — geração de resumos organizados a partir de textos colados pelo aluno
- **Dashboard de estatísticas** — acompanhamento de sessões de estudo, acertos e progresso
- **Tema claro/escuro** — alternância de tema em todo o app, com preferência salva no navegador

---

## Stack utilizada

| Camada         | Tecnologia                          |
|----------------|--------------------------------------|
| Backend        | Java (Servlets + JSP)                |
| Banco de dados | MySQL                                |
| Servidor       | Apache Tomcat                        |
| Build          | Maven                                |
| IA             | Google Gemini API (`gemini-2.5-flash-lite`) |
| Segurança      | BCrypt (hash de senhas)              |
---

## Arquitetura

O projeto segue o padrão **MVC** clássico com Servlets e JSP:

```
├── Servlets          → controle de requisições e regras de rota
├── Services           → orquestração de regras de negócio (ex: ExercicioService, AIService)
├── DAO                → acesso e persistência de dados no MySQL
├── Model              → entidades do domínio (Usuario, Materia, Exercicio, Resposta, SessaoEstudo)
└── JSP (WEB-INF/views) → camada de apresentação
```

### Integração com IA

A comunicação com a API do Gemini é centralizada na classe `AIService`, responsável por:
- Gerar exercícios dissertativos (`gerarExercicios`)
- Corrigir respostas de alunos comparando com o gabarito (`corrigirResposta`)
- Gerar resumos de conteúdo (`gerarResumo`)
- Gerar perguntas avulsas para modo quiz (`gerarPerguntaQuiz`)

Cada funcionalidade faz **uma única chamada HTTP** à API por ação, evitando uso desnecessário de cota.

---

## Autores

Desenvolvido por Miguel e Yuri como projeto acadêmico, com foco em aplicação prática de Java, Servlets, JSP, MySQL e integração com APIs de IA.
