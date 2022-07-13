import * as L from "leaflet";
import {Control} from "leaflet";
import {Pl3xMap} from "../Pl3xMap";

export class LinkControl extends Control {
    private _pl3xmap: Pl3xMap;
    private readonly _dom: HTMLAnchorElement;

    constructor(pl3xmap: Pl3xMap) {
        super();
        this._pl3xmap = pl3xmap;
        super.options = {
            position: 'bottomleft'
        };

        this._dom = L.DomUtil.create('a', 'leaflet-control leaflet-control-button leaflet-control-link');
        this._dom.innerHTML = "<img src='images/clear.png' alt=''/>";
    }

    onAdd(): HTMLAnchorElement {
        this._pl3xmap.map.addEventListener('move', () => this.update());
        this._pl3xmap.map.addEventListener('zoom', () => this.update());
        this.update();

        return this._dom;
    }

    private update(): void {
        this._dom.href = this._pl3xmap.world == null ? '' : this._pl3xmap.getUrlFromView();
    }
}
