import { FlightModel } from '../flights/flightModel';
import { PassengerModel } from '../passengers/passenger.model';

export interface ReservationModel {
  id?: number,
  reservationNumber: string,
  flight: FlightModel,
  seatNumber: string,
  passenger: PassengerModel,
  hasDeparted: boolean
}
