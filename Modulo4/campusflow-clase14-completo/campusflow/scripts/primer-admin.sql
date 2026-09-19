-- El registro público (POST /api/auth/register) SIEMPRE crea usuarios con rol ESTUDIANTE.
-- No existe ningún endpoint que permita auto-asignarse ADMINISTRADOR: por diseño.
--
-- Pasos:
-- 1. Registra un usuario cualquiera con POST /api/auth/register.
-- 2. Ejecuta este UPDATE, cambiando el email por el del usuario que quieres promover.
-- 3. Vuelve a hacer login (POST /api/auth/login) con ese usuario: el token viejo
--    sigue diciendo ESTUDIANTE hasta que se genera uno nuevo.

UPDATE usuarios SET rol = 'ADMINISTRADOR' WHERE email = 'admin@campusflow.com';
