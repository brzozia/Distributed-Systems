import socket
import sys
import threading


def client_fun(client, addr):
    try:
        buff = client.recv(buff_size)
        while buff:
            sid = buff[-2:]     #get id
            msg = buff[:-2]     #get msg
            print('server received msg TCP: ' + '(' + str(int.from_bytes(sid, 'little')) + ') ' + str(msg, 'utf-8'))
            for key in sockets:
                if key != addr:
                    sockets[key].send(buff)
            buff = client.recv(buff_size)

        print('closing connection with ', addr[1])
        if inp != 'close':
            sockets.pop(addr)   # remove socket from dict 
    except:
        print("exception occured 1")
    finally:
        client.close()
        sys.exit()  # close this thread



def tcp_comm_fun():
    while True:
        client, addr = server_socket_tcp.accept()
        client_thread = threading.Thread(target=client_fun, args=(client,addr,), daemon=True)
        sockets[addr] = client
        client_thread.start()


def udp_comm_fun(client):
    try:
        while True:
            buff, uaddr = client.recvfrom(buff_size)
            print('server received msg UDP: ' + '(' + str(uaddr[1]) + ') ' + str(buff, 'utf-8'))
            for key in sockets:
                if key != uaddr:
                    client.sendto(buff+uaddr[1].to_bytes(2,'little'), key)
    except:
        print("exception occured 1")
    finally:
        client.close()


if __name__ == '__main__':
    serverIP = '127.0.0.1'
    serverPort = 9009
    server_socket_tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket_tcp.bind((serverIP, serverPort))
    server_socket_tcp.listen()
    server_socket_udp = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket_udp.bind(('', serverPort))
    buff_size = 1024
    sockets = {}
    inp = ''

    tcp_comm_thread = threading.Thread(target=tcp_comm_fun, daemon=True)
    tcp_comm_thread.start()
    udp_comm_thread = threading.Thread(target=udp_comm_fun, args=(server_socket_udp,), daemon=True)
    udp_comm_thread.start()

    print('PYTHON SERVER')

    while inp != 'close':
        inp = input()

    print('closing all connections')
    for key in sockets:
        sockets[key].shutdown(2)