Chat em Java com Sockets
Fonte
O código foi feito baseado no material do professor:
marcuswac/sd-ufpb/tree/main/labs/lab-sockets (especificamente labs/lab-sockets/src)

Objetivo
O objetivo era criar um chat contínuo que aceitasse vários clientes.

Como Usar
Primeiro, compile os arquivos .java. Depois, inicie o servidor em um terminal e os clientes em outros.

Para iniciar o servidor: java MultiTCPServer

Para iniciar o cliente: java SimpleTCPClient

Importante: Para fazer duas máquinas diferentes se comunicarem, você precisa colocar o IP correto do servidor no código do cliente, junto com a porta. Não use o IP 0.0.0.0 no cliente; use 127.0.0.1 se o servidor estiver na mesma máquina.

O que eu mudei no código:
Editei três classes: SimpleTCPClient, MultiTCPServer e ClientHandler.

SimpleTCPClient.java
O método stop() foi removido. Agora uso try-with-resources no método start para fechar a conexão e os outros recursos automaticamente.

Criei uma Thread separada (listenerThread) só para ficar escutando as mensagens que vêm do servidor e mostrar na tela.

O método start mudou bastante. Ele agora tem o loop principal while(true). A thread principal fica aqui, esperando você digitar uma mensagem e a envia para o servidor. Para sair, é só digitar /exit.

MultiTCPServer.java
A mudança aqui foi pequena. A única coisa que fiz foi adicionar uma List<ClientHandler> para guardar e gerenciar todos os clientes que se conectam ao mesmo tempo.

ClientHandler.java
O construtor foi mudado para receber a lista de clientes (List<ClientHandler>).

O método run() agora tem um loop while(true) para que a conversa não pare depois da primeira mensagem. Ele primeiro pede o nome do usuário e depois fica esperando pelas mensagens.

Adicionei o método broadcastMessage(). A função dele é pegar a mensagem que um cliente enviou e repassar para todos os outros clientes que estão na lista. Ele não envia a mensagem de volta para quem a enviou (sender).
