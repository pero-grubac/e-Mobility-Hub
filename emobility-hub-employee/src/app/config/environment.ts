// API calls are same-origin under /api: Nginx (Docker) or proxy.conf.json
// (ng serve) strips the prefix and forwards them to the backend, so the
// session cookie stays first-party and API paths never clash with routes.
export const environment = {
  baseUrl: '/api',
  cars: '/electric-cars',
  parseVehicle: '/parse-vehicle',
  manufacturers: '/manufacturers',
  bicycles: '/electric-bicycles',
  scooters: '/electric-scooters',
  clients: '/clients',
  employees: '/users',
  faults: '/faults',
  rentals: '/rentals',
  statistics: '/statistics',
  vehicles: '/vehicles',
  auth: '/auth',
};
