import re

# Rutas a los archivos P2 y P5
input_p2 = "im\\feep_p2.pgm"
output_p5 = "im\\feep_p5.pgm"

try:
    # Leer archivo P2
    with open(input_p2, "r") as f:
        lines = f.readlines()

    # Parsear header
    header_lines = []
    pixel_lines = []
    in_header = True
    comment = None

    for line in lines:
        line = line.strip()
        if not line:
            continue
        if line.startswith('#'):
            if in_header and not comment:
                comment = line
            continue
        if in_header:
            header_lines.append(line)
            if len(header_lines) == 3:
                in_header = False
        else:
            pixel_lines.append(line)

    # Validar header
    if len(header_lines) != 3:
        raise ValueError("+ Header PGM invalido.")

    magic_number = header_lines[0]
    if magic_number != "P2":
        raise ValueError(f"+ Formato P2 esperado. Obtenido: {magic_number}.")

    width, height = map(int, re.split(r'\s+', header_lines[1].strip()))
    maxval = int(header_lines[2])
    if maxval < 0 or maxval > 255:
        raise ValueError(f"+ Maximo valor '{maxval}' no soportado.")

    # Parsear valores de pixeles
    pixel_values = []
    for line in pixel_lines:
        values = [int(val) for val in re.split(r'\s+', line.strip()) if val]
        pixel_values.extend(values)

    expected_pixels = width * height
    if len(pixel_values) != expected_pixels:
        raise ValueError(f"+ Pixeles esperados: {expected_pixels}. Obtenidos: {len(pixel_values)}.")

    for val in pixel_values:
        if val < 0 or val > maxval:
            raise ValueError(f"+ Pixel fuera de rango.")

    # Escribir archivo P5
    with open(output_p5, "wb") as f:
        f.write(b"P5\n")
        if comment:
            f.write(f"{comment}\n".encode())
        f.write(f"{width} {height}\n".encode())
        f.write(f"{maxval}\n".encode())
        f.write(bytes(pixel_values))

    print(f"+ Conversion exitosa de '{input_p2}' a '{output_p5}'.")

except FileNotFoundError:
    print(f"+ Error: archivo '{input_p2}' no encontrado.")
except ValueError as e:
    print(f"+ Error: {e}")
except Exception as e:
    print(f"+ Error inesperado: {e}")
