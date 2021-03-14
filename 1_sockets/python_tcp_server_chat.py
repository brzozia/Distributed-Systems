import socket
import sys
import threading


def client_fun(client, addr):
    while True:
        buff = client.recv(buff_size) # tutaj dac kolejke wiadomości???

        if not buff:
            print("closing connection with ", addr[1])
            threads.pop(addr)
            client.close()  
            sys.exit()  # close this thread
        else:
            sid = buff[-2:]     #get id
            msg = buff[:-2]     #get msg
            print("server received msg: " + "(" + str(int.from_bytes(sid, 'little')) + ") " + str(msg, 'utf-8'))

            for key in threads:
                if key != addr:
                    threads[key].send(buff)
            
            buff = ''


def comm_fun():
    while True:
        client, addr = server_socket.accept()
        client_thread = threading.Thread(target=client_fun, args=(client,addr,), daemon=True)
        threads[addr] = client
        client_thread.start()
        print("thread no " + addr[1] + " connected")
    

if __name__ == "__main__":
    serverIP = "127.0.0.1"
    serverPort = 9009
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((serverIP, serverPort))
    server_socket.listen()
    buff_size = 1024
    threads = {}

    comm_thread = threading.Thread(target=comm_fun, daemon=True)
    comm_thread.start()

    print('PYTHON TCP SERVER')

    inp = ''
    while inp != 'close':
        inp = input()

    print("closing all connections")
    for key in threads:
        threads[key].shutdown(2)
        threads[key].close()


