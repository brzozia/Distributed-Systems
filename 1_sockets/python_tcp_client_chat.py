import socket
import struct
import time
import os
import threading

def read_msg(buff):
    sid = buff[-2:]     #get id
    msg = buff[:-2]     #get msg
    return int.from_bytes(sid, 'little'), str(msg, 'utf-8')


def tcp_receive_fun():
    buff = socket_tcp.recv(buff_size)
    while buff:
        sid, msg = read_msg(buff)
        print('(', sid, ') ', msg)
        buff = socket_tcp.recv(buff_size)

    # if server has closed connection
    print('lost connection with server')
    closing_connection()


def udp_m_receive_fun(socket):
    try:
        while True:
            buff, _addr = socket.recvfrom(buff_size)
            sid, msg = read_msg(buff)
            print('(', sid, ') ', msg)
    except:
        print("exception occured")
    finally:
        socket.close()


# def m_receive_fun():
#     while True:
#         buff, _uaddr = socket_udp.recvfrom(buff_size)
#         sid, msg = read_msg(buff)
#         print('(', sid, ') ', msg)
    

def send_fun():
    msg = input() 
    while msg!='close':
        if msg=='U':    #send msg using UDP
            msg = input()
            msg2 = """
                        .-.'  '.-.
                    .-(   \  /   )-.
                    /   '..oOOo..'   |
            ,       \.--.oOOOOOOo.--./
            |\  ,   (   :oOOOOOOo:   )
            _\.\/|   /'--'oOOOOOOo'--'|
            '-.. ;/| \   .''oOOo''.   /
            .--`'. :/|'-(   /  \   )-'
            '--. `. / //'-'.__.'-;
            `'-,_';//      ,  /|
                    '((       |\/./_
                    \\  . |\; ..-'
                    \\ |\: .'`--.
                        \\, .' .--'
                        ))'_,-'`
                jgs  //-'
                    // 
                    //
                    |/
            """
            socket_udp.sendto(bytes(msg,'utf-8'), (serverIP, serverPort))
        elif msg == 'M':
            msg = input()
            socket_m.sendto(bytes(msg,'utf-8')+mname, (multiIP, multiPort))
        else:
            socket_tcp.send(bytes(msg,'utf-8')+sname)    #send msg with sockets id
        msg = input()   #enter new msg
    socket_tcp.shutdown(2)
    

def closing_connection():
    socket_tcp.close()
    socket_udp.close()
    os._exit(0)


if __name__ == '__main__':
    serverIP = '127.0.0.1'
    multiIP = '225.0.0.0'
    serverPort = 9009
    multiPort = 9010
    buff_size = 1024
    socket_m = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    # socket_m.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 1)
    socket_m.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    socket_m.bind(('', multiPort))
    group = socket.inet_aton(multiIP)
    mreq = struct.pack('4sL', group, socket.INADDR_ANY) 
    socket_m.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)
    mname = multiPort.to_bytes(2,'little')

    socket_tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    socket_tcp.connect((serverIP,serverPort))
    port = socket_tcp.getsockname()[1]

    socket_udp = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    socket_udp.bind(('', port))

    sname = port.to_bytes(2,'little')    #get socket id - port number
    print('PYTHON CLIENT id: ' + str(int.from_bytes(sname, 'little')))

    tcp_client_thread = threading.Thread(target=tcp_receive_fun, daemon=True)   #new thread to receive msgs
    tcp_client_thread.start()
    udp_client_thread = threading.Thread(target=udp_m_receive_fun, args=(socket_udp,), daemon=True)   #new thread to receive msgs
    udp_client_thread.start()
    m_client_thread = threading.Thread(target=udp_m_receive_fun, args=(socket_m,), daemon=True)   #new thread to receive msgs
    m_client_thread.start()

    send_fun()
        


