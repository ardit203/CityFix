import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, useMap, useMapEvents } from "react-leaflet";
import { GeoSearchControl, OpenStreetMapProvider } from "leaflet-geosearch";
import "leaflet/dist/leaflet.css";
import "leaflet-geosearch/dist/geosearch.css";

const macedoniaBounds = [
    [40.75, 20.40],
    [42.40, 23.10]
];

const isInsideMacedonia = (latitude, longitude) => {
    const [[south, west], [north, east]] = macedoniaBounds;

    return (
        latitude >= south &&
        latitude <= north &&
        longitude >= west &&
        longitude <= east
    );
};

const SearchControl = ({ onLocationSelect, setPosition }) => {
    const map = useMap();

    useEffect(() => {
        const provider = new OpenStreetMapProvider({
            params: {
                countrycodes: "mk"
            }
        });

        const searchControl = new GeoSearchControl({
            provider,
            style: "bar",
            showMarker: false,
            showPopup: false,
            autoClose: true,
            retainZoomLevel: false,
            animateZoom: true,
            keepResult: true,
            searchLabel: "Search location in Macedonia..."
        });

        map.addControl(searchControl);

        const handleSearchResult = (result) => {
            const latitude = result.location.y;
            const longitude = result.location.x;

            if (!isInsideMacedonia(latitude, longitude)) {
                return;
            }

            const location = {
                latitude,
                longitude,
                address: result.location.label
            };

            setPosition({
                lat: latitude,
                lng: longitude
            });

            onLocationSelect(location);
        };

        map.on("geosearch/showlocation", handleSearchResult);

        return () => {
            map.off("geosearch/showlocation", handleSearchResult);
            map.removeControl(searchControl);
        };
    }, [map, onLocationSelect, setPosition]);

    return null;
};

const ClickHandler = ({ onLocationSelect, setPosition }) => {
    useMapEvents({
        click(event) {
            const latitude = event.latlng.lat;
            const longitude = event.latlng.lng;

            if (!isInsideMacedonia(latitude, longitude)) {
                return;
            }

            const location = {
                latitude,
                longitude
            };

            setPosition(event.latlng);
            onLocationSelect(location);
        }
    });

    return null;
};

const LocationPicker = ({ onLocationSelect }) => {
    const [position, setPosition] = useState(null);

    return (
        <MapContainer
            center={[41.9981, 21.4254]}
            zoom={8}
            minZoom={8}
            maxBounds={macedoniaBounds}
            maxBoundsViscosity={1.0}
            style={{ height: "400px", width: "100%" }}
        >
            <TileLayer
                attribution="&copy; OpenStreetMap contributors"
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            <SearchControl
                onLocationSelect={onLocationSelect}
                setPosition={setPosition}
            />

            <ClickHandler
                onLocationSelect={onLocationSelect}
                setPosition={setPosition}
            />

            {position && <Marker position={position} />}
        </MapContainer>
    );
};

export default LocationPicker;