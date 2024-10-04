import React from "react";
import { Accordion, Container } from "react-bootstrap";

function About() {
	return (
		<Container>
			<Accordion defaultActiveKey="0">
				<Accordion.Item eventKey="0">
					<Accordion.Header>Basic Informations</Accordion.Header>
					<Accordion.Body>
						Student name: CuongNM
						<br />
						Student ID: SE170070
						<br />
						Email: cuongnmse170070@fpt.edu.vn
					</Accordion.Body>
				</Accordion.Item>
				<Accordion.Item eventKey="1">
					<Accordion.Header>Other Informations</Accordion.Header>
					<Accordion.Body>LAB 1, 2, 3, 4 made by CuongNM.</Accordion.Body>
				</Accordion.Item>
			</Accordion>
		</Container>
	);
}

export default About;
