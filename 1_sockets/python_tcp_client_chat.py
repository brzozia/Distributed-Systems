import socket
import time
import os
import threading

def receive_fun():
    buff = client.recv(buff_size)
    
    while buff:
        sid = buff[-2:]     #get id
        msg = buff[:-2]     #get msg
        print("(" + str(int.from_bytes(sid, 'little')) + ") "  + str(msg, 'utf-8') )
        buff = client.recv(buff_size)

    # if server has closed connection
    print("lost connection with server")
    closing_connection()


def send_fun(msg):
    while msg!='close':
        client.send(bytes(msg,'utf-8')+sname)    #send msg with sockets id
        msg = input()   #enter new msg
    client.shutdown(2)
    closing_connection()
    

def closing_connection():
    client.close()
    os._exit(0)


if __name__ == "__main__":
    serverIP = "127.0.0.1"
    serverPort = 9009
    msg = "witaj!"
    buff_size = 1024
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((serverIP,serverPort))

    sname = client.getsockname()[1].to_bytes(2,'little')    #get socket id - port number
    print('PYTHON TCP CLIENT id: ' + str(int.from_bytes(sname, 'little')))

    client_thread = threading.Thread(target=receive_fun, daemon=True)   #new thread to receive msgs
    client_thread.start()

    send_fun(msg)
        


