import { FlightModel } from '../flights/FlightModel';
import { PassengerModel } from '../passengers/passenger.model';

export interface ReservationModel {
  reservationId: number,
  reservationNumber: string,
  flight: FlightModel,
  seatNumber: string,
  passenger: PassengerModel,
  hasDeparted: boolean
}
