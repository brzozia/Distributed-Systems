import socket
import time
import threading

def receive_fun():
    while True:
        buff = client.recv(buff_size)
        if not buff:
            client.close()
            break
        else:
            print("client received msg:", str(buff,'utf-8'))
            buff = ''




serverIP = "127.0.0.1"
serverPort = 9009
msg = "żółta gęś"
buff_size = 1024
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((serverIP,serverPort))

print('PYTHON TCP CLIENT')

client_thread = threading.Thread(target=receive_fun, daemon=True)
client_thread.start()
msg = 1
while msg!='close':
    client.send(bytes(msg,'utf-8'))
    # msg = input("enter:")
    time.sleep(1)
    msg+=1
    

client.close()
client_thread._stop()


