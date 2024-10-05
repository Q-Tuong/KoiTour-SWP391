import React from "react";
import { Container } from "react-bootstrap";
import Koi from "../components/Koi";
import { KoiList } from "../data/KoiList";
import NavBar from "../components/NavBar";

function KoiPage() {
	return (
		<>
			<NavBar />
			<Container>
				<h2>Koi to buy</h2>
				<Koi koilist={KoiList} />
			</Container>
		</>
	);
}

export default KoiPage;
