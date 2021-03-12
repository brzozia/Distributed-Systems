import socket
from queue import Queue
import threading


def client_fun(client, addr):
    while True:
        buff = client.recv(buff_size)
        print("received "+ str(buff, 'utf-8'))

        if not buff:
            client.close()
            threads.pop(addr)
            break
        else:
            for key in threads:
                if key != addr:
                    threads[key].send(buff)

serverIP = "127.0.0.1"
serverPort = 9009
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((serverIP, serverPort))
buff_size = 1024
threads = {}

client_thread = threading.Thread(target=client_fun, args=(client,addr,), daemon=True)
client_thread.start()

print('PYTHON TCP SERVER')

while True:
    server_socket.listen()
    client, addr = server_socket.accept()
    client_thread = threading.Thread(target=client_fun, args=(client,addr,), daemon=True)
    threads[addr] = client
    client_thread.start()
    print("thread connected")


