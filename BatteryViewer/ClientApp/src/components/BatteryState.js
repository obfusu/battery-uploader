import React, { Component } from 'react';

export class BatteryState extends Component {
    static displayName = BatteryState.name;

    constructor(props) {
        super(props);
        this.refresh.bind(this);
        this.state = { percentage: 0 };
    }

    componentDidMount() {
        this.refresh();

        this.interval = setInterval(this.refresh, 10000);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    render() {
        return (
            <div>
                <h1>{this.state.percentage}</h1>
            </div>
        );
    }

    refresh = () => {
        const apiUrl = 'http://localhost:5000/api/battery';
        fetch(apiUrl)
            .then(response => {
                response.json().then(data => {
                    console.log(data);
                    this.setState({ percentage: data.percentage })
                });
            });
    }
}
