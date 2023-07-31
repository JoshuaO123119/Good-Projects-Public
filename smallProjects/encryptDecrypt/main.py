from cryptography.fernet import Fernet
import json

def writeInfo(key, token, filePath):
    with open(filePath, "r+") as jsonFile:
        data = dict(json.load(jsonFile))
        data["token"] = token.decode()
        data["key"] = str(key.decode())

        jsonFile.seek(0)
        jsonFile.truncate(0)
        jsonFile.write(json.dumps(data, indent=4))
        jsonFile.close()
def readInfo(filePath):
    with open(filePath, "r") as file:
        info = dict(json.load(file))
        token = info["token"].encode()
        key = Fernet(info["key"].encode())

        file.close()
    return token, key

encryptMessage = b"Test123"

# Generate key
key = Fernet.generate_key()
# Make key an object
f = Fernet(key)
# Encrypt the message
token = f.encrypt(encryptMessage)

# Write the string key and encrypted message into data.json
writeInfo(key, token, "data.json")
# Grab token and key data from data.json
token, key = readInfo("data.json")

# Decrypt the message using the key and token, then turn it from byte to string
print(key.decrypt(token).decode())