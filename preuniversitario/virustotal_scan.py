#!/usr/bin/env python3
import requests
import time
import sys
import os
import hashlib
import json

# Configuración
API_KEY = os.environ.get('VT_API_KEY')  # Obtener la API key desde variables de entorno
FILE_PATH = sys.argv[1] if len(sys.argv) > 1 else None
MAX_RETRIES = 5
THRESHOLD = 3  # Número máximo de detecciones permitidas

if not API_KEY:
    print("Error: No se ha proporcionado la API key de VirusTotal")
    sys.exit(1)

if not FILE_PATH:
    print("Error: No se ha especificado la ruta del archivo")
    sys.exit(1)

if not os.path.exists(FILE_PATH):
    print(f"Error: El archivo {FILE_PATH} no existe")
    sys.exit(1)

# Calcular el hash SHA256 del archivo
def get_file_hash(file_path):
    sha256_hash = hashlib.sha256()
    with open(file_path, "rb") as f:
        for byte_block in iter(lambda: f.read(4096), b""):
            sha256_hash.update(byte_block)
    return sha256_hash.hexdigest()

file_hash = get_file_hash(FILE_PATH)

# Comprobar si el archivo ya ha sido analizado
def check_hash(file_hash):
    url = f"https://www.virustotal.com/api/v3/files/{file_hash}"
    headers = {
        "x-apikey": API_KEY
    }
    
    response = requests.get(url, headers=headers)
    
    if response.status_code == 200:
        return response.json()
    elif response.status_code == 404:
        return None
    else:
        print(f"Error al comprobar el hash: {response.status_code}")
        print(response.text)
        sys.exit(1)

# Subir el archivo para análisis
def upload_file(file_path):
    url = "https://www.virustotal.com/api/v3/files"
    headers = {
        "x-apikey": API_KEY
    }
    
    with open(file_path, "rb") as file:
        files = {"file": (os.path.basename(file_path), file)}
        response = requests.post(url, headers=headers, files=files)
    
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Error al subir el archivo: {response.status_code}")
        print(response.text)
        sys.exit(1)

# Esperar y obtener el resultado del análisis
def get_analysis_results(file_hash):
    for i in range(MAX_RETRIES):
        result = check_hash(file_hash)
        
        if result and "data" in result:
            stats = result["data"]["attributes"]["last_analysis_stats"]
            detections = stats.get("malicious", 0) + stats.get("suspicious", 0)
            
            print(f"Resultado del análisis:")
            print(f"- Malicioso: {stats.get('malicious', 0)}")
            print(f"- Sospechoso: {stats.get('suspicious', 0)}")
            print(f"- No detectado: {stats.get('undetected', 0)}")
            print(f"- Tiempo de espera: {stats.get('timeout', 0)}")
            
            return detections
        
        print(f"Esperando resultados... (intento {i+1}/{MAX_RETRIES})")
        time.sleep(60)  # Esperar 60 segundos entre intentos
    
    print("No se pudieron obtener los resultados después de varios intentos")
    sys.exit(1)

# Verificar si el archivo ya ha sido analizado
result = check_hash(file_hash)

if not result:
    print(f"El archivo no ha sido analizado previamente. Subiendo archivo...")
    upload_result = upload_file(FILE_PATH)
    print("Archivo subido correctamente. Esperando análisis...")

# Obtener y evaluar los resultados
detections = get_analysis_results(file_hash)

if detections > THRESHOLD:
    print(f"¡ALERTA! El archivo ha sido detectado como malicioso por {detections} motores")
    print(f"Enlace al análisis: https://www.virustotal.com/gui/file/{file_hash}")
    sys.exit(1)
else:
    print(f"El archivo es seguro (detectado por solo {detections} motores, por debajo del umbral de {THRESHOLD})")
    print(f"Enlace al análisis: https://www.virustotal.com/gui/file/{file_hash}")
    sys.exit(0)
