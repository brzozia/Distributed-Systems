import socket;

serverIP = "127.0.0.1"
serverPort = 9009
msg = "żółta gęś"
buff = []
buff_size = 1024

print('PYTHON TCP CLIENT')
while True:

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((serverIP,serverPort))

    client.send(msg)
    data = client.recv(buff_size)

    client.close()

    print("python tcp client received msg: ", data)




