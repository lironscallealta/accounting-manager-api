================================================================================
1. GUÍA DE COMMITS CONVENCIONALES (CONVENTIONAL COMMITS)
================================================================================

Formato básico:
tipo(alcance_opcional): descripción corta en minúsculas y en modo imperativo

--------------------------------------------------------------------------------
LOS TIPOS DE COMMIT MÁS IMPORTANTES:
--------------------------------------------------------------------------------

feat:     Se usa cuando agregas una NUEVA FUNCIONALIDAD al proyecto.
          Ejemplo: git commit -m "feat: agregar controlador para login de usuarios"

fix:      Se usa cuando solucionas un ERROR (bug) en el código.
          Ejemplo: git commit -m "fix: corregir error 500 al calcular total de factura"

refactor: Cambios que mejoran la estructura o calidad del código sin cambiar su
          comportamiento (ni agrega funciones ni arregla bugs).
          Ejemplo: git commit -m "refactor: optimizar la consulta SQL de reportes"

style:    Cambios que no afectan el significado del código (formateo de texto,
          espacios, sangrías, añadir puntos y comas que faltaban).
          Ejemplo: git commit -m "style: formatear clases de java según reglas"

docs:     Cambios únicamente en la documentación (archivos README, Javadoc, etc.).
          Ejemplo: git commit -m "docs: actualizar instrucciones en el readme"

chore:    Tareas rutinarias de mantenimiento que no tocan el código fuente
          (modificar .gitignore, instalar dependencias, actualizar el pom.xml).
          Ejemplo: git commit -m "chore: agregar dependencia de spring security"

test:     Cuando se agregan, modifican o corrigen pruebas unitarias o de integración.
          Ejemplo: git commit -m "test: agregar pruebas para el servicio contable"

--------------------------------------------------------------------------------
REGLAS DE ORO PARA EL MENSAJE:
--------------------------------------------------------------------------------
1. Usa el verbo en infinitivo/imperativo (ej: "agregar" en vez de "agregué").
2. Escribe la descripción todo en minúsculas.
3. No pongas punto final al terminar la frase.
