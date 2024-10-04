import React from "react";
import { Container } from "react-bootstrap";
import Koi from "../components/Koi";
import { KoiList } from "../data/KoiList";

function KoiPage() {
	return (
		<Container>
			<h2>Koi to buy</h2>
			<Koi koilist={KoiList} />
		</Container>
	);
}

export default KoiPage;
