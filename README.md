Aplicación de reserva de taxis iCar:

-buscar taxis disponibles y realizar reservas: función de búsqueda que permita a los usuarios encontrar viajes. El proceso de reserva implicará la integración de la pasarela de pago Razorpay.

-panel completo donde los usuarios podrán ver su historial de viajes, verificar los detalles del viaje y monitorear el progreso de sus viajes en curso. Actualizaciones en tiempo real de las reservas.

-registro de conductores

-panel del conductor proporcionará una descripción de los viajes asignados, lo que permitirá a los conductores aceptar, rechazar e iniciar viajes según su disponibilidad.

-verificación basada en OTP cuando los conductores inician un viaje.

-cálculo los ingresos del conductor en función de los viajes completados.

-los conductores tendrán acceso a su historial de viajes, lo que les permitirá realizar un seguimiento de sus ganancias y obtener información sobre los viajes realizados.

Tecnologías utilizadas:
-Vite.js
-Spring Boot
-Razorpay
-Tailwind CSS
-Material-UI
-Formik (creación de formularios)
-MySQL


##Endpoints de la aplicación iCar

DriverController:

    1. GET ("/api/drivers/profile")
    Descripción: Obtiene el perfil del conductor requerido.
    Encabezado requerido: Authorization (contiene el JWT).

    2. GET ("/api/drivers/{driverId}/current_ride")
    Descripción: Obtiene el viaje actual del conductor con el ID proporcionado.
    Parámetro de ruta: driverId.

    3. GET ("/api/drivers/{driverId}/allocated")
    Descripción: Obtiene las rides asignadas al conductor con el ID proporcionado.
    Parámetro de ruta: driverId.

    4. GET ("/api/drivers/rides/completed")
    Descripción: Obtiene las rides completadas por el conductor requerido.
    Encabezado requerido: Authorization (contiene el JWT).


AuthController:

    1. POST ("/api/auth/user/signup")
    Descripción: Registro de un nuevo usuario.
    Cuerpo de la solicitud: SignupRequest.

    2.POST ("/api/auth/driver/signup")
    Descripción: Registro de un nuevo conductor.
    Cuerpo de la solicitud: DriverSignupRequest.

    3. POST ("/api/auth/signin")
    Descripción: Inicio de sesión de un usuario. 
    Cuerpo de la solicitud: LoginRequest.

HomeController:

    1. GET ("/")
    Descripción: Este endpoint proporciona un mensaje de bienvenida a los usuarios que visitan la raíz de la aplicación.
    Respuesta: Un objeto MessageResponse con el mensaje "Welcome to iCar".


RideController

   1. Solicitar un viaje
   Método HTTP: POST
   Ruta: /api/rides/request
   Descripción: Permite a un usuario solicitar un viaje.
   Parámetros: RideRequest rideRequest en el cuerpo de la solicitud.
  Authorization en el encabezado de la solicitud.
   Respuesta: RideDTO con los detalles del viaje solicitado.
   Código de Estado HTTP: 202 Accepted

   2. Aceptar un viaje
   Método HTTP: PUT
   Ruta: /api/rides/{rideId}/accept
   Descripción: Permite aceptar un viaje pendiente.
   Parámetros: rideId en la ruta de la solicitud.
   Respuesta: MessageResponse con el mensaje "Ride Accepted".
   Código de Estado HTTP: 202 Accepted

3. Rechazar un viaje
   Método HTTP: PUT
   Ruta: /api/rides/{rideId}/decline
   Descripción: Permite a un conductor rechazar un viaje pendiente.
   Parámetros:
   Authorization en el encabezado de la solicitud.
   rideId en la ruta de la solicitud.
   Respuesta: MessageResponse con el mensaje "Ride Declined".
   Código de Estado HTTP: 202 Accepted

4. Iniciar un viaje
   Método HTTP: PUT
   Ruta: /api/rides/{rideId}/start
   Descripción: Permite a un conductor iniciar un viaje.
   Parámetros: rideId en la ruta de la solicitud.
   StartRideRequest request en el cuerpo de la solicitud.
   Respuesta: MessageResponse con el mensaje "Ride Started".
   Código de Estado HTTP: 202 Accepted

5. Obtener detalles de un viaje
   Método HTTP: GET
   Ruta: /api/rides/{rideId}
   Descripción: Permite obtener los detalles de un viaje específico.
   Parámetros: rideId en la ruta de la solicitud.
   Authorization en el encabezado de la solicitud.
   Respuesta: RideDTO con los detalles del viaje.
   Código de Estado HTTP: 202 Accepted

6. Completar un viaje
   Método HTTP: PUT
   Ruta: /api/rides/{rideId}/complete
   Descripción: Permite marcar un viaje como completado.
   Parámetros: rideId en la ruta de la solicitud.
   Respuesta: MessageResponse con el mensaje "Ride Completed".
   Código de Estado HTTP: 202 Accepted

UserController:

1. Encontrar un usuario por su ID
   Método HTTP: GET
   Ruta: /api/users/{userId}
   Descripción: Permite encontrar un usuario por su ID.
   Parámetros: userId en la ruta de la solicitud.
   Respuesta: User con los detalles del usuario encontrado.
   Código de Estado HTTP: 202 Accepted

2. Obtener el perfil del usuario autenticado
   Método HTTP: GET
   Ruta: /api/users/profile
   Descripción: Permite obtener el perfil del usuario autenticado mediante un token JWT.
   Parámetros: Authorization en el encabezado de la solicitud.
   Respuesta: User con los detalles del perfil del usuario autenticado.
   Código de Estado HTTP: 202 Accepted

3. Obtener la lista de viajes completados por el usuario
   Método HTTP: GET
   Ruta: /api/users/rides/completed
   Descripción: Permite obtener la lista de viajes completados por el usuario autenticado.
   Parámetros: Authorization en el encabezado de la solicitud.
   Respuesta: List<Ride> con la lista de viajes completados por el usuario.
   Código de Estado HTTP: 202 Accepted

interface User {
fullName: string;
email: string
password: string
role: UserRole
mobile: string;
id: Integer
profilePicture: string
}

interface Driver {
id: Integer
email: string
name: string
mobile: string
rating: double
latitude: double
longitude: double
role: UserRole
password: string
license: License
rides: List<Ride>
vehicle: Vehicle
currentRide: Ride
totalRevenue: Integer
}

Interface License {
id: Integer
licenseNumbre: String
licenseState: string
licenseExpirationDate: String
driver: Driver
}

interface Notification{
id: Integer
message: string
timestamp: LocalDateTime
user: User
driver: Driver
ride: Ride
notificationType: NotificationType
}

interface PaymentDetails {
paymentStatus: PaymentStatus
paymentId: string
razorpayPaymentLinkId: string
razorpayPaymentLinkReferenceId: string
razorpayPaymentLinkStatus: string
razorpayPaymentId: string
}

interface Ride {
id: Integer
user: User
driver: Driver
declinedDrivers: List<Integer>
pickupLatitude: double
pickupLongitude: double
destinationLatitude: double
destinationLongitude: double
pickUpArea: string
destinationArea: string
status: RideStatus
startTime: LocalDateTime
endTime: LocalDateTime
fare: double
otp: int
distance: double
duration: long
paymentDetails: paymentDetails
}

interface Vehicle {
id: Integer
make: string
model: string
year: int
color: string
licensePlate: string
capacity: int
driver: Driver
}
