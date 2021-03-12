import socket;

serverIP = "127.0.0.1"
serverPort = 9009
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((serverIP, serverPort))
server_socket.listen(1)
buff = []
buff_size = 1024

client, addr = server_socket.accept()

print('PYTHON TCP SERVER')

while True:

    buff = client.recv(buff_size)
    client.send("hi")
    print("python udp server received msg: " + str(buff, 'cp1250'))

client.close()

