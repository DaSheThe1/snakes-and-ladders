# first of all import the socket library
import socket
import json


# next create a socket object
s = socket.socket()
print("Socket successfully created")

# reserve a port on your computer in our
# case it is 12345 but it can be anything
port = 1000

# Next bind to the port
# we have not typed any ip in the ip field
# instead we have inputted an empty string
# this makes the server listen to requests
# coming from other computers on the network
s.bind(('0.0.0.0', port))
print("socket bound to %s" % port)

# put the socket into listening mode
s.listen(5)
print("socket is listening")

# a forever loop until we interrupt it or
# an error occurs


def save_user(username, password):
    with open(r'E:\Python experiment\userjason.txt', 'r') as f1:
        userjson = json.loads(f1.read())

    if not username in userjson:
        userjson[username] = password
        with open(r'E:\Python experiment\userjason.txt', 'w') as f1:
            f1.write(json.dumps(userjson))
        return "new user have been created"
    else:
        if password == userjson.get(username):
            return "password match"
        else:
            return "password doesnt match"


while True:
    # Establish connection with client.
    c, address = s.accept()
    print('Got connection from', address)

    # receive a massage from the client
    username = c.recv(1024)
    password = c.recv(1024)

    print username[1:], password[1:]

    c.send(save_user(username[1:], password[1:]))

    # Close the connection with the client
    c.close()

s.close()
