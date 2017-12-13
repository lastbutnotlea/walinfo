import Partei from './Partei';

export default class Kandidat {
  id: number;
  titel: string;
  name: string;
  vorname: string;
  namneszusatz: string;
  geburtsjahr: number;
  partei: Partei;
}
