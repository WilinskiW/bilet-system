export interface FlightModel{
  flightId?: number,
  departurePlace: string,
  arrivalPlace: string,
  duration: number,
  flightNumber: string,
  departureTime: Date,
  roundTrip: boolean
}
