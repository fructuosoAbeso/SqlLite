Servidor de Base de Datos (Room)

¿Qué hace esta App?

Es el "Servidor" que gestiona la base de datos de usuarios. Permite crear, actualizar, listar y eliminar usuarios, además de ofrecer un sistema de Backup.

Tecnologías utilizadas
Room Persistence Library: Para la gestión de la base de datos local (migrado desde SQLite puro).

Content Provider: Para permitir que otras apps consulten los datos de usuario.

Executors: Para realizar operaciones de base de datos fuera del hilo principal.

Errores solucionados
Crash por MainThread: La app se cerraba al intentar acceder a la DB en el hilo de la interfaz. Solución: Implementamos Executors.newSingleThreadExecutor().

Error "Unit.password": Kotlin no reconocía las propiedades de los usuarios. Solución: Corregimos el UsuarioDao añadiendo tipos de retorno específicos (: UsuarioEntity?).

Error de Provider en Manifest: La App 2 no encontraba el Provider. Solución: Corregimos el android:name en el Manifest para que apuntara a la ruta exacta del archivo.
