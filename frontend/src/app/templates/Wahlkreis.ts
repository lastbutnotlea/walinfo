import Bundesland from './Bundesland';

export default class Wahlkreis {
  id: number;
  nummer: number;
  name: string;
  bundesland: Bundesland;
  corrNummer: number;
}
