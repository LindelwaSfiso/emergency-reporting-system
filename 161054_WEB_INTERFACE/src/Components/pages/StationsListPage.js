import React, {useEffect, useState} from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "../home/Drawer";
import Divider from "@mui/material/Divider";
import { DataGrid } from '@mui/x-data-grid';
import {collection, doc, getDocs, onSnapshot} from "firebase/firestore";
import {db} from "../../config/firebaseConfig";


const columns = [
    {field: 'stationName', headerName: 'Station Name', width: 200 },
    {field: 'stationTelephone', headerName: 'Telephone', width: 200 },
    {
        field: 'stationType',
        headerName: 'Station Type',
        width: 200,
        valueGetter: ({value}) => {
            switch (value) {
                case 10:
                    return "MEDICAL";
                case 11:
                    return "POLICE";
                default:
                    return "FIRE"
            }
        }
    },
    {field: 'stationCoordinates', headerName: 'Coordinates', width: 200}
];

const data = [
    {
        id: 0,
        stationCoordinates : "-26.480380, 31.306819",
        stationName: "Matsapha Police Station",
        stationTelephone: "76480479",
        stationType: 11,
        uid : "lFqk5RuIO2VeleUGIpVoDwg5giy2"
    },
    {
        id: 1,
        stationCoordinates : "-26.480380, 31.306819",
        stationName: "Matsapha Police Station",
        stationTelephone: "76480479",
        stationType: 11,
        uid : "lFqk5RuIO2VeleUGIpVoDwg5giy2"
    },
    {
        id: 2,
        stationCoordinates : "-26.480380, 31.306819",
        stationName: "Matsapha Police Station",
        stationTelephone: "76480479",
        stationType: 11,
        uid : "lFqk5RuIO2VeleUGIpVoDwg5giy2"
    },
    {
        id: 3,
        stationCoordinates : "-26.480380, 31.306819",
        stationName: "Matsapha Police Station",
        stationTelephone: "76480479",
        stationType: 11,
        uid : "lFqk5RuIO2VeleUGIpVoDwg5giy2"
    },
    {
        id: 4,
        stationCoordinates : "-26.480380, 31.306819",
        stationName: "Matsapha Police Station",
        stationTelephone: "76480479",
        stationType: 11,
        uid : "lFqk5RuIO2VeleUGIpVoDwg5giy2"
    },
]

export default function StationsListPage() {
    const [stations, setStations] = useState([])

    /*useEffect(() => {
        const unSub = async () => {
            await getDocs(collection(db, "EMERGENCY_STATIONS")).then((a) => {
                a.forEach((document) => {
                    console.log(document.data())
                })
            })
        }

        return () => {
            unSub()
        }
    }, [setStations])*/

    return (
        <React.Fragment>
            <Drawer>
                <Typography className={"welcome-text"} variant="h5" color="primary" component={"h5"}>
                    List of registered emergency stations.
                </Typography>
                <Divider />
                <div style={{ width: '100%', marginTop: '15px' }}>
                    <DataGrid
                        rows={data}
                        columns={columns}
                        pageSize={5}
                        rowsPerPageOptions={[5]}
                        // checkboxSelection
                        autoHeight
                        autoPageSize
                    />
                </div>
            </Drawer>
        </React.Fragment>
    );
}

