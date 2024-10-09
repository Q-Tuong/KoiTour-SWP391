import React from "react";
import { Button, Table } from "react-bootstrap";
import { Link } from "react-router-dom";

function Tour({ tourlist }) {
	return (
		<Table striped bordered hover>
			<thead>
				<tr>
					<th>#</th>
					<th>Tour Name</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Detail</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				{tourlist.map((tour) => (
					<tr key={tour.tourId}>
						<td>{tour.tourId}</td>
						<td>{tour.name}</td>
						<td>{tour.dateStart}</td>
						<td>{tour.dateEnd}</td>
						<td>
							<Link to={`/TourDetail/${tour.tourId}`}>Detail</Link>
						</td>
						<td>
							<Button variant="primary">Book now</Button>
						</td>
					</tr>
				))}
			</tbody>
		</Table>
	);
}

export default Tour;
